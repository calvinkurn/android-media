package com.tokopedia.minicart.common.widget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.cartlist.MiniCartListViewHolderMapper
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.uimodel.MiniCartWidgetUiModel
import kotlinx.coroutines.*
import javax.inject.Inject

class MiniCartWidgetViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                  private val getMiniCartWidgetDataUseCase: GetMiniCartWidgetDataUseCase,
                                                  private val getMiniCartListUseCase: GetMiniCartListUseCase,
                                                  private val deleteCartUseCase: DeleteCartUseCase,
                                                  private val undoDeleteCartUseCase: UndoDeleteCartUseCase,
                                                  private val updateCartUseCase: UpdateCartUseCase,
                                                  private val miniCartListViewHolderMapper: MiniCartListViewHolderMapper)
    : BaseViewModel(executorDispatchers.main) {

    private val _currentShopIds = MutableLiveData<List<String>>()
    val currentShopIds: LiveData<List<String>>
        get() = _currentShopIds

    private val _globalEvent = MutableLiveData<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    private val _miniCartWidgetUiModel = MutableLiveData<MiniCartWidgetUiModel>()
    val miniCartWidgetUiModel: LiveData<MiniCartWidgetUiModel>
        get() = _miniCartWidgetUiModel

    private val _miniCartListUiModel = MutableLiveData<MiniCartListUiModel>()
    val miniCartListListUiModel: LiveData<MiniCartListUiModel>
        get() = _miniCartListUiModel

    private val tmpHiddenUnavailableItems = mutableListOf<Visitable<*>>()

    private var lastDeletedProductItem = mutableMapOf<Int, MiniCartProductUiModel>()

    fun getLatestWidgetState(shopIds: List<String>) {
        _currentShopIds.value = shopIds
        getMiniCartWidgetDataUseCase.setParams(shopIds)
        getMiniCartWidgetDataUseCase.execute(onSuccess = {
            _miniCartWidgetUiModel.value = MiniCartWidgetUiModel(
                    state = MiniCartWidgetUiModel.STATE_NORMAL,
                    totalProductCount = it.data.totalProductCount,
                    totalProductPrice = it.data.totalProductPrice,
                    totalProductError = it.data.totalProductError
            )
        }, onError = {
            _miniCartWidgetUiModel.value = MiniCartWidgetUiModel(
                    state = MiniCartWidgetUiModel.STATE_NORMAL,
                    totalProductCount = 0,
                    totalProductPrice = 0,
                    totalProductError = 0
            )
        })
    }

    fun getCartList() {
        val shopIds = currentShopIds.value ?: emptyList()
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(
                onSuccess = {
                    _miniCartListUiModel.value = miniCartListViewHolderMapper.mapUiModel(it)
                },
                onError = {
                    _miniCartListUiModel.value = MiniCartListUiModel().apply {
                        title = "Belanjaanmu di TokoNOW!"
                    }
                }
        )
    }

    fun updateProductQty(productId: String, newQty: Int) {
        val visitables = miniCartListListUiModel.value?.visitables ?: emptyList()
        loop@ for (visitable in visitables) {
            if (visitable is MiniCartProductUiModel && visitable.productId == productId && !visitable.isProductDisabled) {
                visitable.productQty = newQty
                break@loop
            }
        }
    }

    fun calculateProduct() {
        val visitables = miniCartListListUiModel.value?.visitables ?: mutableListOf()

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
                visitable.parentId = (++tmpParentId).toString()
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
        var totalPrice = 0L
        var totalValue = 0L
        var totalDiscount = 0L
        var totalWeight = 0
        miniCartProductList.forEach { visitable ->
            val price = if (visitable.productWholeSalePrice > 0) visitable.productWholeSalePrice else visitable.productPrice
            totalQty += visitable.productQty
            totalPrice += visitable.productQty * price
            val originalPrice = if (visitable.productOriginalPrice > 0) visitable.productOriginalPrice else visitable.productPrice
            totalValue += visitable.productQty * originalPrice
            totalWeight += visitable.productQty * visitable.productWeight
            val discountValue = if (visitable.productOriginalPrice > 0) visitable.productOriginalPrice - visitable.productPrice else 0
            totalDiscount += visitable.productQty * discountValue
            miniCartListListUiModel.value?.let {
                it.miniCartWidgetUiModel.totalProductPrice = totalPrice
                it.miniCartWidgetUiModel.totalProductCount = totalQty
                it.miniCartSummaryTransactionUiModel.qty = totalQty
                it.miniCartSummaryTransactionUiModel.totalValue = totalValue
                it.miniCartSummaryTransactionUiModel.discountValue = totalDiscount
                it.miniCartSummaryTransactionUiModel.paymentTotal = totalPrice
            }
        }

        validateOverWeight(totalWeight, visitables)

        miniCartListListUiModel.value?.visitables = visitables
        _miniCartListUiModel.value = miniCartListListUiModel.value
    }

    private fun setWholesalePrice(miniCartProductList: MutableList<MiniCartProductUiModel>, productParentQtyMap: MutableMap<String, Int>, visitables: MutableList<Visitable<*>>) {
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
        val maxWeight = miniCartListListUiModel.value?.maximumShippingWeight ?: 0

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

            val warningWording = miniCartListListUiModel.value?.maximumShippingWeightErrorMessage
                    ?: ""
            val overWeight = (totalWeight - maxWeight) / 1000.0f
            if (tickerWarning == null) {
                tickerWarning = miniCartListViewHolderMapper.mapTickerWarningUiModel(overWeight, warningWording)
                tickerWarning.let {
                    val firstItem = visitables.firstOrNull()
                    if (firstItem != null && firstItem is MiniCartTickerErrorUiModel) {
                        visitables.add(1, it)
                    } else {
                        visitables.add(0, it)
                    }
                }
            } else {
                val updatedTickerWarning = tickerWarning!!.deepCopy()
                updatedTickerWarning.warningMessage = warningWording.replace("{{weight}}", overWeight.toString())
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

    fun handleUnavailableItemsAccordion() {
        val visitables = miniCartListListUiModel.value?.visitables?.toMutableList()
                ?: mutableListOf()
        var accordionUiModel: MiniCartAccordionUiModel? = null
        var indexAccordionUiModel: Int = -1
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartAccordionUiModel) {
                accordionUiModel = visitable
                indexAccordionUiModel = index
                break@loop
            }
        }

        accordionUiModel?.let {
            if (it.isCollapsed) {
                expandUnavailableItems(visitables, it, indexAccordionUiModel)
            } else {
                collapseUnavailableItems(visitables, it, indexAccordionUiModel)
            }
        }
    }

    private fun collapseUnavailableItems(visitables: MutableList<Visitable<*>>, accordionUiModel: MiniCartAccordionUiModel, indexAccordionUiModel: Int) {
        val tmpUnavailableProducts = mutableListOf<Visitable<*>>()
        visitables.forEachIndexed { index, visitable ->
            if (visitable is MiniCartUnavailableReasonUiModel || (visitable is MiniCartProductUiModel && visitable.isProductDisabled)) {
                tmpUnavailableProducts.add(visitable)
            }
        }

        if (tmpUnavailableProducts.size > 2) {
            val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
                isCollapsed = !isCollapsed
            }
            visitables[indexAccordionUiModel] = updatedAccordionUiModel

            tmpUnavailableProducts.removeFirst() // exclude first reason
            tmpUnavailableProducts.removeFirst() // exclude first unavailable item
            tmpHiddenUnavailableItems.clear()
            tmpHiddenUnavailableItems.addAll(tmpUnavailableProducts)
            visitables.removeAll(tmpUnavailableProducts)

            miniCartListListUiModel.value?.visitables = visitables
            _miniCartListUiModel.value = miniCartListListUiModel.value
        }
    }

    private fun expandUnavailableItems(visitables: MutableList<Visitable<*>>, accordionUiModel: MiniCartAccordionUiModel, indexAccordionUiModel: Int) {
        val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
            isCollapsed = !isCollapsed
        }
        visitables[indexAccordionUiModel] = updatedAccordionUiModel

        visitables.addAll(indexAccordionUiModel - 1, tmpHiddenUnavailableItems)
        tmpHiddenUnavailableItems.clear()

        miniCartListListUiModel.value?.visitables = visitables
        _miniCartListUiModel.value = miniCartListListUiModel.value
    }

/*
    fun removeCartItemById(cartIds: List<String>, context: Context?): Pair<ArrayList<Int>, ArrayList<Int>> {
        val visitables = miniCartListListUiModel.value?.visitables?.toMutableList() ?: emptyList<>()

        // Store item first before remove item to prevent ConcurrentModificationException
        val toBeRemovedData = ArrayList<Any>()
        val toBeRemovedIndex = ArrayList<Int>()
        val toBeUpdatedIndex = ArrayList<Int>()
        var disabledItemHeaderHolderData: MiniCartUnavailableHeaderUiModel? = null
        var cartItemTickerErrorHolderData: MiniCartTickerErrorUiModel? = null
        var disabledAccordionHolderData: MiniCartAccordionUiModel? = null

        // Get to be deleted items
        var deletedDisabledItemCount = 0
        loop@ for (i in visitables.indices) {
            val obj = visitables[i]
            when (obj) {
                // For enable / available item
                is MiniCartProductUiModel -> {
                    if (!obj.isProductDisabled) {
                        if (cartIds.contains(obj.cartId)) {
                            toBeRemovedData.add(obj)
                            toBeRemovedIndex.add(i)
                        }
                    } else {
                        if (cartIds.contains(obj.cartId)) {
                            if (i < 1) {
                                continue@loop
                            }
                            val indexBefore = i - 1
                            val before = visitables[indexBefore]
                            var after: Any? = null
                            if (i + 1 < visitables.size) {
                                after = visitables[i + 1]
                            }

                            if (before is MiniCartProductUiModel && before.isProductDisabled) {
                                toBeRemovedData.add(obj)
                                toBeRemovedIndex.add(i)
                            }

                            // If two item before `obj` is reason item, then remove it since the reason has only one shop and the shop has only one item
                            if (i < 2) {
                                continue@loop
                            }
                            val indexTwoBefore = i - 2
                            val twoBefore = visitables[indexTwoBefore]
                            if (twoBefore is MiniCartUnavailableReasonUiModel) {
                                if (after !is MiniCartProductUiModel) {
                                    toBeRemovedData.add(twoBefore)
                                    toBeRemovedIndex.add(indexTwoBefore)
                                }
                            }

                            deletedDisabledItemCount++

                        }
                    }
                }

                is MiniCartUnavailableHeaderUiModel -> disabledItemHeaderHolderData = obj
                is MiniCartTickerErrorUiModel -> cartItemTickerErrorHolderData = obj
                is MiniCartAccordionUiModel -> disabledAccordionHolderData = obj
            }
        }

        // Remove all collected items from previous loop
        for (cartShopHolderData in toBeRemovedData) {
            cartDataList.remove(cartShopHolderData)
        }

        // Remove select all item if all available item removed
        if (selectAllHolderData != null && allAvailableCartItemData.isEmpty()) {
            toBeRemovedIndex.add(cartDataList.indexOf(selectAllHolderData))
            cartDataList.remove(selectAllHolderData)
        }

        // Check if delete action is bulk delete on unavailable section. If true, remove accordion
        if (deletedDisabledItemCount != 0 && cartIds.size > 1) {
            disabledAccordionHolderData?.let {
                toBeRemovedData.add(it)
            }
            tmpCollapsedItem.clear()
        }

        // Determine to remove error ticker and unavailable item header
        if (cartItemTickerErrorHolderData != null || disabledItemHeaderHolderData != null) {
            // Count available item and unavailable items
            var errorItemCount = 0
            var normalItemCount = 0
            loop@ for (any in cartDataList) {
                when (any) {
                    is CartShopHolderData -> any.shopGroupAvailableData?.cartItemDataList?.let {
                        for (cartItemHolderData in it) {
                            normalItemCount++
                        }
                    }
                    is DisabledCartItemHolderData -> errorItemCount++
                    is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            errorItemCount += collapsedCartItemData.size

            if (errorItemCount > 0) {
                // Goes here if unavailable item still exist
                if (context != null) {
                    if (normalItemCount == 0) {
                        // If normal / non error item empty, remove unavailable item error ticker
                        cartItemTickerErrorHolderData?.let {
                            toBeRemovedIndex.add(cartDataList.indexOf(it))
                            cartDataList.remove(it)
                        }
                    } else {
                        // If normal / non error item not empty, adjust error ticker item wording count
                        cartItemTickerErrorHolderData?.let {
                            it.cartTickerErrorData?.errorInfo = String.format(context.getString(R.string.cart_error_message), errorItemCount)
                            toBeUpdatedIndex.add(cartDataList.indexOf(it))
                        }
                    }
                    // Adjust unavailable item header wording
                    disabledItemHeaderHolderData?.let {
                        it.disabledItemCount = errorItemCount
                        toBeUpdatedIndex.add(cartDataList.indexOf(it))
                    }
                }
            } else {
                // Goes here if unavailable item is not exist
                // Remove unavailable item error ticker
                cartItemTickerErrorHolderData?.let {
                    toBeRemovedIndex.add(cartDataList.indexOf(it))
                    cartDataList.remove(it)
                }
                // Remove unavailable item header
                disabledItemHeaderHolderData?.let {
                    toBeRemovedIndex.add(cartDataList.indexOf(it))
                    visitables.remove(it)
                }
            }
        }

        cartItemTickerErrorHolderData?.let {
            toBeUpdatedIndex.add(visitables.indexOf(it))
        }

        disabledItemHeaderHolderData?.let {
            toBeUpdatedIndex.add(visitables.indexOf(it))
        }

        return Pair(toBeRemovedIndex, toBeUpdatedIndex)
    }
*/

    fun singleDeleteCartItems(product: MiniCartProductUiModel) {
        deleteCartUseCase.setParams(listOf(product))
        deleteCartUseCase.execute(
                onSuccess = {
                    handleDelete(product)
/*
                    var visitables = miniCartListListUiModel.value?.visitables ?: mutableListOf()
                    var deletedIndex: Int = -1

                    // Delete item
                    loop@ for ((index, visitable) in visitables.withIndex()) {
                        if (visitable is MiniCartProductUiModel && visitable.cartId == product.cartId) {
                            deletedIndex = index
                            break@loop
                        }
                    }

                    val deletedItem = visitables[deletedIndex] as MiniCartProductUiModel
                    lastDeletedProductItem.clear()
                    lastDeletedProductItem[deletedIndex] = deletedItem

                    // Validate to delete other for available items
                    if (!deletedItem.isProductDisabled) {
                        var hasAvailableItem = false
                        loop@ for ((index, visitable) in visitables.withIndex()) {
                            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled && deletedIndex != index) {
                                hasAvailableItem = true
                                break@loop
                            }
                            if (visitable is MiniCartUnavailableHeaderUiModel) {
                                hasAvailableItem = false
                                visitables = visitables.subList(index, visitables.size)
                            }
                        }

                        if (!hasAvailableItem) {
                            miniCartListListUiModel.value?.visitables = visitables
                            _miniCartListUiModel.value = miniCartListListUiModel.value

                            _globalEvent.value = GlobalEvent(GlobalEvent.STATE_SUCCESS_DELETE_ALL_AVAILABLE_CART_ITEM)
                            calculateProduct()
                        } else {
                            if (deletedIndex != -1) {
                                visitables.removeAt(deletedIndex)
                                miniCartListListUiModel.value?.visitables = visitables
                                _miniCartListUiModel.value = miniCartListListUiModel.value

                                _globalEvent.value = GlobalEvent(GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM)
                                calculateProduct()
                            }
                        }
                    } else {
                        var isForceToExpand = false
                        loop@ for (visitable in visitables) {
                            if (visitable is MiniCartAccordionUiModel && visitable.isCollapsed) {
                                handleUnavailableItemsAccordion()
                                isForceToExpand = true
                                break@loop
                            }
                        }

                        val firstPreviousItem = visitables[deletedIndex - 1]
                        val nextItem = visitables[deletedIndex + 1]
                        if (nextItem is) {

                        }

                    }
*/
                },
                onError = {
                    _globalEvent.value = GlobalEvent(GlobalEvent.STATE_FAILED_DELETE_CART_ITEM)
                }
        )
    }

    private fun handleDelete(product: MiniCartProductUiModel) {
        val visitables = miniCartListListUiModel.value?.visitables ?: mutableListOf()
        val tmpVisitables = miniCartListListUiModel.value?.visitables ?: mutableListOf()
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartProductUiModel && visitable.cartId == product.cartId) {
                tmpVisitables.removeAt(index)

                val deletedItem = visitables[index] as MiniCartProductUiModel
                lastDeletedProductItem.clear()
                lastDeletedProductItem[index] = deletedItem

                miniCartListListUiModel.value?.visitables = tmpVisitables
                _miniCartListUiModel.value = miniCartListListUiModel.value

                _globalEvent.value = GlobalEvent(GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM)
                break@loop
            }
        }
    }

    fun getUnavailableItems(): MutableList<MiniCartProductUiModel> {
        val unavailableProducts = mutableListOf<MiniCartProductUiModel>()
        val visitables = miniCartListListUiModel.value?.visitables?.toMutableList() ?: emptyList()
        visitables.forEach {
            if (it is MiniCartProductUiModel && it.isProductDisabled) {
                unavailableProducts.add(it)
            }
        }

        return unavailableProducts
    }

    fun bulkDeleteUnavailableCartItems() {
        val unavailableCartItems = mutableListOf<MiniCartProductUiModel>()
        miniCartListListUiModel.value?.visitables?.forEach {
            if (it is MiniCartProductUiModel && it.isProductDisabled) {
                unavailableCartItems.add(it)
            }
        }
        deleteCartUseCase.setParams(unavailableCartItems)
        deleteCartUseCase.execute(
                onSuccess = {
                    _globalEvent.value = GlobalEvent(GlobalEvent.STATE_SUCCESS_BULK_DELETE_UNAVAILABLE_ITEMS)
                    getCartList()
                    calculateProduct()
                },
                onError = {
                    _globalEvent.value = GlobalEvent(GlobalEvent.STATE_FAILED_BULK_DELETE_UNAVAILABLE_ITEMS)
                }
        )
    }

    fun undoDeleteCartItems() {
        if (lastDeletedProductItem.isNotEmpty()) {
            for ((index, value) in lastDeletedProductItem) {
                undoDeleteCartUseCase.setParams(value.cartId)
                undoDeleteCartUseCase.execute(
                        onSuccess = {
                            getCartList()
                        },
                        onError = {

                        }
                )
            }
        }
    }

    fun updateCart(isForCheckout: Boolean = false) {
        val productUiModels = mutableListOf<MiniCartProductUiModel>()
        miniCartListListUiModel.value?.visitables?.forEach {
            if (it is MiniCartProductUiModel && !it.isProductDisabled) {
                productUiModels.add(it)
            }
        }
        updateCartUseCase.setParamsFromUiModels(productUiModels)
        updateCartUseCase.execute(
                onSuccess = {
                    if (isForCheckout) {
                        _globalEvent.value = GlobalEvent(GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT)
                    }
                },
                onError = {
                    if (isForCheckout) {
                        _globalEvent.value = GlobalEvent(GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT)
                    }
                }
        )
    }

}