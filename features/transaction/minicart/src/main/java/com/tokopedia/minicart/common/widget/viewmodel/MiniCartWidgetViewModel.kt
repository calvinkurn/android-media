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

    private var lastDeletedProductItem: MiniCartProductUiModel? = null

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

    fun getCartList(isFirstLoad: Boolean = false) {
        val shopIds = currentShopIds.value ?: emptyList()
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(
                onSuccess = {
                    _miniCartListUiModel.value = miniCartListViewHolderMapper.mapUiModel(it)
                },
                onError = {
                    if (isFirstLoad) {
                        _globalEvent.value = GlobalEvent(state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET)
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

    fun updateProductNotes(productId: String, newNotes: String) {
        val visitables = miniCartListListUiModel.value?.visitables ?: emptyList()
        loop@ for (visitable in visitables) {
            if (visitable is MiniCartProductUiModel && visitable.productId == productId && !visitable.isProductDisabled) {
                visitable.productNotes = newNotes
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
                val updatedTickerWarning = tickerWarning.deepCopy()
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

    fun singleDeleteCartItems(product: MiniCartProductUiModel) {
        deleteCartUseCase.setParams(listOf(product))
        deleteCartUseCase.execute(
                onSuccess = {
                    handleDelete(product)
                },
                onError = {
                    _globalEvent.value = GlobalEvent(state = GlobalEvent.STATE_FAILED_DELETE_CART_ITEM)
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
                lastDeletedProductItem = deletedItem

                miniCartListListUiModel.value?.visitables = tmpVisitables
                _miniCartListUiModel.value = miniCartListListUiModel.value

                _globalEvent.value = GlobalEvent(state = GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM)
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
                    _globalEvent.value = GlobalEvent(state = GlobalEvent.STATE_SUCCESS_BULK_DELETE_UNAVAILABLE_ITEMS)
                    getCartList()
                    calculateProduct()
                },
                onError = {
                    _globalEvent.value = GlobalEvent(state = GlobalEvent.STATE_FAILED_BULK_DELETE_UNAVAILABLE_ITEMS)
                }
        )
    }

    fun undoDeleteCartItems() {
        lastDeletedProductItem?.let {
            undoDeleteCartUseCase.setParams(it.cartId)
            undoDeleteCartUseCase.execute(
                    onSuccess = {
                        getCartList()
                    },
                    onError = {
                        getCartList()
                    }
            )
        }
    }

    fun updateCart(isForCheckout: Boolean = false, observer: Int = GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
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
                        if (it.data.status) {
                            _globalEvent.value = GlobalEvent(
                                    observer = observer,
                                    state = GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT
                            )
                        } else {
                            _globalEvent.value = GlobalEvent(
                                    observer = observer,
                                    state = GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT,
                                    data = it.data
                            )
                        }
                    }
                },
                onError = {
                    if (isForCheckout) {
                        _globalEvent.value = GlobalEvent(
                                observer = observer,
                                state = GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT
                        )
                    }
                }
        )
    }

}