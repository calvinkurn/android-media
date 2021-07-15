package com.tokopedia.minicart.common.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cart_common.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.cart_common.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.cart_common.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.RemoveFromCartDomainModel
import com.tokopedia.minicart.common.domain.usecase.*
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class MiniCartViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                            private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
                                            private val getMiniCartListUseCase: GetMiniCartListUseCase,
                                            private val deleteCartUseCase: DeleteCartUseCase,
                                            private val undoDeleteCartUseCase: UndoDeleteCartUseCase,
                                            private val updateCartUseCase: UpdateCartUseCase,
                                            private val miniCartListUiModelMapper: MiniCartListUiModelMapper)
    : BaseViewModel(executorDispatchers.main) {

    companion object {
        const val TEMPORARY_PARENT_ID_PREFIX = "tmp_"
    }

    // Global Data
    private val _currentShopIds = MutableLiveData<List<String>>()
    val currentShopIds: LiveData<List<String>>
        get() = _currentShopIds

    private val _currentPage = MutableLiveData<MiniCartAnalytics.Page>()
    val currentPage: LiveData<MiniCartAnalytics.Page>
        get() = _currentPage

    // Widget DATA
    private val _globalEvent = MutableLiveData<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    // Bottom Sheet Data
    private val _miniCartListBottomSheetUiModel = MutableLiveData<MiniCartListUiModel>()
    val miniCartListBottomSheetUiModel: LiveData<MiniCartListUiModel>
        get() = _miniCartListBottomSheetUiModel

    val tmpHiddenUnavailableItems = mutableListOf<Visitable<*>>()

    var lastDeletedProductItem: MiniCartProductUiModel? = null
        private set

    // Used for mocking _miniCartListBottomSheetUiModel value.
    // Should only be called from unit test.
    fun setMiniCartListUiModel(miniCartListUiModel: MiniCartListUiModel) {
        _miniCartListBottomSheetUiModel.value = miniCartListUiModel
    }

    // Used for mocking _miniCartSimplifiedData value.
    // Should only be called from unit test.
    fun setMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    // Used for mocking lastDeletedProductItem value.
    // Should only be called from unit test.
    fun setLastDeleteProductItem(miniCartProductUiModel: MiniCartProductUiModel) {
        lastDeletedProductItem = miniCartProductUiModel
    }

    // Setter & Getter
    // Some of setter & getter here added to reduce complexity (due to nullability) so we can increase unit test coverage

    fun initializeCurrentPage(currentPage: MiniCartAnalytics.Page) {
        _currentPage.value = currentPage
    }

    fun initializeShopIds(shopIds: List<String>) {
        _currentShopIds.value = shopIds
    }

    fun initializeGlobalState() {
        _globalEvent.value = GlobalEvent()
    }

    fun updateMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    fun getLatestMiniCartData(): MiniCartSimplifiedData {
        return miniCartListUiModelMapper.reverseMapUiModel(miniCartListBottomSheetUiModel.value, tmpHiddenUnavailableItems)
    }

    fun resetTemporaryHiddenUnavailableItems() {
        tmpHiddenUnavailableItems.clear()
    }

    private fun getVisitables(): MutableList<Visitable<*>> {
        return miniCartListBottomSheetUiModel.value?.visitables ?: mutableListOf()
    }

    private fun getShopIds(): List<String> {
        return currentShopIds.value ?: emptyList()
    }

    private fun getMiniCartItems(): List<MiniCartItem> {
        return miniCartSimplifiedData.value?.miniCartItems ?: emptyList()
    }

    private fun updateVisitables(visitables: MutableList<Visitable<*>>) {
        miniCartListBottomSheetUiModel.value?.visitables = visitables
        _miniCartListBottomSheetUiModel.value = miniCartListBottomSheetUiModel.value
    }

    // API Call & Callback

    fun getLatestWidgetState(shopIds: List<String>? = null) {
        if (shopIds != null) {
            initializeShopIds(shopIds)
            getMiniCartListSimplifiedUseCase.setParams(shopIds)
        } else {
            val tmpShopIds = getShopIds()
            getMiniCartListSimplifiedUseCase.setParams(tmpShopIds)
        }
        getMiniCartListSimplifiedUseCase.execute(onSuccess = {
            _miniCartSimplifiedData.value = it
        }, onError = {
            if (miniCartSimplifiedData.value != null) {
                _miniCartSimplifiedData.value = miniCartSimplifiedData.value
            } else {
                _miniCartSimplifiedData.value = MiniCartSimplifiedData()
            }
        })
    }

    fun getCartList(isFirstLoad: Boolean = false) {
        val shopIds = getShopIds()
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(
                onSuccess = {
                    onSuccessGetCartList(it, isFirstLoad)
                },
                onError = {
                    onErrorGetCartList(isFirstLoad, it)
                }
        )
    }

    private fun onSuccessGetCartList(miniCartData: MiniCartData, isFirstLoad: Boolean) {
        if (isFirstLoad && miniCartData.data.outOfService.id.isNotBlank() && miniCartData.data.outOfService.id != "0") {
            _globalEvent.value = GlobalEvent(
                    state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET,
                    data = miniCartData
            )
        } else {
            val tmpMiniCartListUiModel = miniCartListUiModelMapper.mapUiModel(miniCartData)
            tmpMiniCartListUiModel.isFirstLoad = isFirstLoad
            tmpMiniCartListUiModel.needToCalculateAfterLoad = true
            _miniCartListBottomSheetUiModel.value = tmpMiniCartListUiModel
        }
    }

    private fun onErrorGetCartList(isFirstLoad: Boolean, throwable: Throwable) {
        if (isFirstLoad) {
            _globalEvent.value = GlobalEvent(
                    state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET,
                    throwable = throwable
            )
        }
    }

    fun deleteSingleCartItem(product: MiniCartProductUiModel) {
        deleteCartUseCase.setParamsFromUiModel(listOf(product))
        deleteCartUseCase.execute(
                onSuccess = {
                    onSuccessDeleteSingleCartItem(product, it)
                },
                onError = {
                    onErrorDeleteSingleCartItem(it)
                }
        )
    }

    private fun onSuccessDeleteSingleCartItem(product: MiniCartProductUiModel, removeFromCartData: RemoveFromCartData) {
        val visitables = getVisitables()
        val tmpVisitables = getVisitables()
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartProductUiModel && visitable.cartId == product.cartId) {
                val deletedItem = visitables[index] as MiniCartProductUiModel
                lastDeletedProductItem = deletedItem

                tmpVisitables.removeAt(index)

                updateVisitables(tmpVisitables)

                var isLastItem = true
                innerLoop@ for (item in tmpVisitables) {
                    if (item is MiniCartProductUiModel) {
                        isLastItem = false
                        break@innerLoop
                    }
                }

                _globalEvent.value = GlobalEvent(
                        state = GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM,
                        data = RemoveFromCartDomainModel(removeFromCartData = removeFromCartData, isLastItem = isLastItem, isBulkDelete = false)
                )
                break@loop
            }
        }
    }

    private fun onErrorDeleteSingleCartItem(throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_DELETE_CART_ITEM,
                throwable = throwable
        )
    }

    fun bulkDeleteUnavailableCartItems() {
        val unavailableCartItems = mutableListOf<MiniCartProductUiModel>()
        val visitables = getVisitables()
        visitables.forEach {
            if (it is MiniCartProductUiModel && it.isProductDisabled) {
                unavailableCartItems.add(it)
            }
        }

        val tmpVisitables = getVisitables()
        var isLastItem = true
        loop@ for (item in tmpVisitables) {
            if (item is MiniCartProductUiModel && !item.isProductDisabled) {
                isLastItem = false
                break@loop
            }
        }

        deleteCartUseCase.setParamsFromUiModel(unavailableCartItems)
        deleteCartUseCase.execute(
                onSuccess = {
                    onSuccessBulkDeleteUnavailableCartItems(it, isLastItem)
                },
                onError = {
                    onErrorBulkDeleteUnavailableCartItems(it)
                }
        )
    }

    private fun onSuccessBulkDeleteUnavailableCartItems(removeFromCartData: RemoveFromCartData, isLastItem: Boolean) {
        _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM,
                data = RemoveFromCartDomainModel(removeFromCartData = removeFromCartData, isLastItem = isLastItem, isBulkDelete = true)
        )
    }

    private fun onErrorBulkDeleteUnavailableCartItems(throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_DELETE_CART_ITEM,
                throwable = throwable
        )
    }

    fun undoDeleteCartItem(isLastItem: Boolean) {
        lastDeletedProductItem?.let { miniCartProductUiModel ->
            undoDeleteCartUseCase.setParams(miniCartProductUiModel.cartId)
            undoDeleteCartUseCase.execute(
                    onSuccess = {
                        onSuccessUndoDeleteCartItem(it, isLastItem)
                    },
                    onError = {
                        onErrorUndoDeleteCartItem(it)
                    }
            )
        }
    }

    private fun onSuccessUndoDeleteCartItem(undoDeleteCartDataResponse: UndoDeleteCartDataResponse, isLastItem: Boolean) {
        lastDeletedProductItem = null
        _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM,
                data = UndoDeleteCartDomainModel(undoDeleteCartDataResponse, isLastItem)
        )
    }

    private fun onErrorUndoDeleteCartItem(throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM,
                throwable = throwable
        )
    }

    fun updateCart(isForCheckout: Boolean, observer: Int) {
        var source = ""
        if (!isForCheckout) {
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
        }

        if (observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
            val miniCartItems = mutableListOf<MiniCartItem>()
            val allMiniCartItem = getMiniCartItems()
            allMiniCartItem.forEach {
                if (!it.isError) {
                    miniCartItems.add(it)
                }
            }
            updateCartUseCase.setParams(miniCartItems, true, source)
        } else if (observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            val miniCartProductUiModels = mutableListOf<MiniCartProductUiModel>()
            val visitables = getVisitables()
            visitables.forEach {
                if (it is MiniCartProductUiModel && !it.isProductDisabled) {
                    miniCartProductUiModels.add(it)
                }
            }
            updateCartUseCase.setParamsFromUiModels(miniCartProductUiModels, source)
        }
        updateCartUseCase.execute(
                onSuccess = {
                    onSuccessUpdateCart(isForCheckout, it, observer)
                },
                onError = {
                    onErrorUpdateCart(isForCheckout, observer, it)
                }
        )
    }

    private fun onSuccessUpdateCart(isForCheckout: Boolean, updateCartV2Data: UpdateCartV2Data, observer: Int) {
        if (isForCheckout) {
            if (updateCartV2Data.data.status) {
                _globalEvent.value = GlobalEvent(
                        observer = observer,
                        state = GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT
                )
            } else {
                _globalEvent.value = GlobalEvent(
                        observer = observer,
                        state = GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT,
                        data = updateCartV2Data.data
                )
            }
        }
    }

    private fun onErrorUpdateCart(isForCheckout: Boolean, observer: Int, throwable: Throwable) {
        if (isForCheckout) {
            _globalEvent.value = GlobalEvent(
                    observer = observer,
                    state = GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT,
                    throwable = throwable
            )
        }
    }

    // User Interaction

    fun updateProductQty(productId: String, newQty: Int) {
        val visitables = getVisitables()
        loop@ for (visitable in visitables) {
            if (visitable is MiniCartProductUiModel && visitable.productId == productId && !visitable.isProductDisabled) {
                visitable.productQty = newQty
                break@loop
            }
        }

        val cartItems = getMiniCartItems()
        loop@ for (cartItem in cartItems) {
            if (cartItem.productId == productId && !cartItem.isError) {
                cartItem.quantity = newQty
                break@loop
            }
        }
    }

    fun updateProductNotes(productId: String, newNotes: String) {
        val visitables = getVisitables()
        loop@ for (visitable in visitables) {
            if (visitable is MiniCartProductUiModel && visitable.productId == productId && !visitable.isProductDisabled) {
                visitable.productNotes = newNotes
                break@loop
            }
        }

        val cartItems = getMiniCartItems()
        loop@ for (cartItem in cartItems) {
            if (cartItem.productId == productId && !cartItem.isError) {
                cartItem.notes = newNotes
                break@loop
            }
        }
    }

    fun toggleUnavailableItemsAccordion() {
        val visitables = getVisitables()
        var miniCartAccordionUiModel: MiniCartAccordionUiModel? = null
        var indexAccordionUiModel: Int = -1
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartAccordionUiModel) {
                miniCartAccordionUiModel = visitable
                indexAccordionUiModel = index
                break@loop
            }
        }

        miniCartAccordionUiModel?.let { accordionUiModel ->
            if (accordionUiModel.isCollapsed) {
                expandUnavailableItems(visitables, accordionUiModel, indexAccordionUiModel)
            } else {
                collapseUnavailableItems(visitables, accordionUiModel, indexAccordionUiModel)
            }
        }
    }

    private fun collapseUnavailableItems(visitables: MutableList<Visitable<*>>, accordionUiModel: MiniCartAccordionUiModel, indexAccordionUiModel: Int) {
        val tmpUnavailableProducts = mutableListOf<Visitable<*>>()
        visitables.forEach { visitable ->
            if (visitable is MiniCartUnavailableReasonUiModel || (visitable is MiniCartProductUiModel && visitable.isProductDisabled)) {
                tmpUnavailableProducts.add(visitable)
            }
        }

        if (tmpUnavailableProducts.size > 2) {
            val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
                isCollapsed = true
            }
            visitables[indexAccordionUiModel] = updatedAccordionUiModel

            tmpUnavailableProducts.removeFirst() // exclude first reason
            tmpUnavailableProducts.removeFirst() // exclude first unavailable item
            tmpHiddenUnavailableItems.clear()
            tmpHiddenUnavailableItems.addAll(tmpUnavailableProducts)
            visitables.removeAll(tmpUnavailableProducts)

            updateVisitables(visitables)
        }
    }

    private fun expandUnavailableItems(visitables: MutableList<Visitable<*>>, accordionUiModel: MiniCartAccordionUiModel, indexAccordionUiModel: Int) {
        val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
            isCollapsed = false
        }
        visitables[indexAccordionUiModel] = updatedAccordionUiModel

        visitables.addAll(indexAccordionUiModel - 1, tmpHiddenUnavailableItems)
        tmpHiddenUnavailableItems.clear()

        updateVisitables(visitables)
    }


    // Calculation

    fun calculateProduct() {
        val visitables = getVisitables()

        val miniCartProductList = mutableListOf<MiniCartProductUiModel>()
        visitables.forEach { visitable ->
            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled) {
                miniCartProductList.add(visitable)
            }
        }

        // Set temporary parent id for non variant product
        var tmpParentId = 0
        miniCartProductList.forEach { visitable ->
            if (visitable.parentId == "0") {
                visitable.parentId = TEMPORARY_PARENT_ID_PREFIX + ++tmpParentId
            }
        }

        // Map of parent id - qty
        val productParentQtyMap = mutableMapOf<String, Int>()
        miniCartProductList.forEach { visitable ->
            if (productParentQtyMap.containsKey(visitable.parentId)) {
                val newQty = (productParentQtyMap[visitable.parentId] ?: 0) + visitable.productQty
                productParentQtyMap[visitable.parentId] = newQty
            } else {
                productParentQtyMap[visitable.parentId] = visitable.productQty
            }
        }

        // Set wholesale price
        setWholesalePrice(miniCartProductList, productParentQtyMap, visitables)

        // Calculate total price
        var totalQty = 0
        var sellerCashbackValue = 0L
        var totalPrice = 0L
        var totalValue = 0L
        var totalDiscount = 0L
        var totalWeight = 0
        visitables.forEach { visitable ->
            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled) {
                if (visitable.parentId.contains(TEMPORARY_PARENT_ID_PREFIX)) visitable.parentId = "0"
                totalQty += visitable.productQty
                val price =
                        when {
                            visitable.productWholeSalePrice > 0 -> visitable.productWholeSalePrice
                            else -> visitable.productPrice
                        }
                totalPrice += visitable.productQty * price
                sellerCashbackValue += (visitable.productQty * visitable.productCashbackPercentage / 100.0 * price).toLong()
                val originalPrice =
                        when {
                            visitable.productOriginalPrice > 0 -> visitable.productOriginalPrice
                            visitable.productWholeSalePrice > 0 -> visitable.productWholeSalePrice
                            else -> visitable.productPrice
                        }
                totalValue += visitable.productQty * originalPrice
                val discountValue =
                        when {
                            visitable.productOriginalPrice > 0 -> visitable.productOriginalPrice - visitable.productPrice
                            else -> 0
                        }
                totalDiscount += visitable.productQty * discountValue
                totalWeight += visitable.productQty * visitable.productWeight
            }
        }
        miniCartListBottomSheetUiModel.value?.let {
            it.miniCartWidgetUiModel.totalProductPrice = totalPrice
            it.miniCartWidgetUiModel.totalProductCount = totalQty
            it.miniCartSummaryTransactionUiModel.qty = totalQty
            it.miniCartSummaryTransactionUiModel.totalValue = totalValue
            it.miniCartSummaryTransactionUiModel.discountValue = totalDiscount
            it.miniCartSummaryTransactionUiModel.paymentTotal = totalPrice
            it.miniCartSummaryTransactionUiModel.sellerCashbackValue = sellerCashbackValue
            it.needToCalculateAfterLoad = false
        }

        validateOverWeight(totalWeight, visitables)

        updateVisitables(visitables)
    }

    private fun setWholesalePrice(miniCartProductList: MutableList<MiniCartProductUiModel>,
                                  productParentQtyMap: MutableMap<String, Int>,
                                  visitables: MutableList<Visitable<*>>) {
        val updatedProductForWholesalePriceItems = mutableListOf<MiniCartProductUiModel>()
        val updatedProductForWholesalePriceItemsProductId = mutableListOf<String>()
        miniCartProductList.forEach { visitable ->
            val updatedProduct = visitable.deepCopy()
            var isUpdatedWholeSalePrice = false // flag to update ui
            var isEligibleForWholesalePrice = false
            loop@ for (wholesalePrice in visitable.wholesalePriceGroup) {
                val qty = productParentQtyMap[visitable.parentId] ?: 0

                // Set wholesale price if eligible
                if (qty >= wholesalePrice.qtyMin) {
                    if (updatedProduct.productWholeSalePrice != wholesalePrice.prdPrc) {
                        updatedProduct.productWholeSalePrice = wholesalePrice.prdPrc
                        isUpdatedWholeSalePrice = true
                    }
                    isEligibleForWholesalePrice = true
                    break@loop
                }
            }

            // Reset wholesale price not eligible and previously has wholesale price
            if (!isEligibleForWholesalePrice && visitable.productWholeSalePrice > 0L) {
                updatedProduct.productWholeSalePrice = 0
                isUpdatedWholeSalePrice = true
            }

            if (isUpdatedWholeSalePrice) {
                updatedProductForWholesalePriceItems.add(updatedProduct)
                updatedProductForWholesalePriceItemsProductId.add(updatedProduct.productId)
            }
        }

        val updatedProductIndex = mutableListOf<Int>()
        visitables.forEachIndexed { index, visitable ->
            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled && updatedProductForWholesalePriceItemsProductId.contains(visitable.productId)) {
                updatedProductIndex.add(index)
            }
        }

        var tmpIndex = 0
        updatedProductIndex.forEach { index ->
            val tmpVisitable = updatedProductForWholesalePriceItems[tmpIndex++]
            visitables[index] = tmpVisitable
        }
    }

    private fun validateOverWeight(totalWeight: Int, visitables: MutableList<Visitable<*>>) {
        val maxWeight = miniCartListBottomSheetUiModel.value?.maximumShippingWeight ?: 0

        if (totalWeight > maxWeight) {
            var tickerWarning: MiniCartTickerWarningUiModel? = null
            var tickerWarningIndex = -1
            loop@ for ((index, visitable) in visitables.withIndex()) {
                if (visitable is MiniCartTickerWarningUiModel) {
                    tickerWarning = visitable
                    tickerWarningIndex = index
                    break@loop
                }
            }

            val warningWording = miniCartListBottomSheetUiModel.value?.maximumShippingWeightErrorMessage
                    ?: ""
            val overWeight = (totalWeight - maxWeight) / 1000.0f
            if (tickerWarning == null) {
                tickerWarning = miniCartListUiModelMapper.mapTickerWarningUiModel(overWeight, warningWording)
                tickerWarning.let {
                    val firstItem = visitables.firstOrNull()
                    if (firstItem != null && firstItem is MiniCartTickerErrorUiModel) {
                        visitables.add(1, it)
                    } else {
                        visitables.add(0, it)
                    }
                }
            } else {
                val updatedTickerWarning = tickerWarning.deepCopy()
                val formattedOverWeight = NumberFormat.getNumberInstance(Locale("in", "id")).format(overWeight)
                updatedTickerWarning.warningMessage = warningWording.replace(MiniCartListUiModelMapper.PLACEHOLDER_OVERWEIGHT_VALUE, "$formattedOverWeight ")
                visitables[tickerWarningIndex] = updatedTickerWarning
            }
        } else {
            removeTickerWarning(visitables)
        }
    }

    private fun removeTickerWarning(visitables: MutableList<Visitable<*>>) {
        var tmpIndex = -1
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartTickerWarningUiModel) {
                tmpIndex = index
                break@loop
            }
        }

        if (tmpIndex != -1) {
            visitables.removeAt(tmpIndex)
        }
    }

}