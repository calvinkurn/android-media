package com.tokopedia.minicart.cartlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import javax.inject.Inject

class MiniCartListViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                private val getMiniCartListUseCase: GetMiniCartListUseCase,
                                                private val miniCartListViewHolderMapper: MiniCartListViewHolderMapper) : BaseViewModel(executorDispatchers.main) {

    private val _miniCartUiModel = MutableLiveData<MiniCartUiModel>()
    val miniCartUiModel: LiveData<MiniCartUiModel>
        get() = _miniCartUiModel

    private val tmpHiddenUnavailableItems = mutableListOf<Visitable<*>>()

    fun getCartList(shopIds: List<String>) {
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(onSuccess = {
            _miniCartUiModel.value = miniCartListViewHolderMapper.mapUiModel(it)
        }, onError = {
            _miniCartUiModel.value = MiniCartUiModel().apply {
                title = "Belanjaanmu di TokoNOW!"
            }
        })
    }

    fun calculateProduct() {
        val visitables = miniCartUiModel.value?.visitables ?: mutableListOf()

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
                val newQty = productParentQtyMap[visitable.parentId] ?: 0 + visitable.productQty
                productParentQtyMap[visitable.parentId] = newQty
            } else {
                productParentQtyMap[visitable.parentId] = visitable.productQty
            }
        }

        // Set wholesale price
        miniCartProductList.forEach { visitable ->
            visitable.wholesalePriceGroup.forEach wholesaleLoop@{ wholesalePrice ->
                val qty = productParentQtyMap[visitable.parentId] ?: 0
                if (qty >= wholesalePrice.qtyMin) {
                    visitable.productWholeSalePrice = wholesalePrice.prdPrc
                    return@wholesaleLoop
                }
            }
        }

        // Calculate total price
        var totalQty = 0
        var totalPrice = 0L
        var totalWeight = 0
        miniCartProductList.forEach { visitable ->
            val price = if (visitable.productWholeSalePrice > 0) visitable.productWholeSalePrice else visitable.productPrice
            totalQty += visitable.productQty
            totalPrice += visitable.productQty * price
            totalWeight += visitable.productQty * visitable.productWeight
            miniCartUiModel.value?.miniCartWidgetData?.totalProductPrice = totalPrice
            miniCartUiModel.value?.miniCartWidgetData?.totalProductCount = totalQty
        }

        validateOverWeight(totalWeight, visitables)

        miniCartUiModel.value?.visitables = visitables
        _miniCartUiModel.value = miniCartUiModel.value
    }

    private fun validateOverWeight(totalWeight: Int, visitables: MutableList<Visitable<*>>) {
        val maxWeight = miniCartUiModel.value?.maximumShippingWeight ?: 0

        if (totalWeight > maxWeight) {
            var tickerWarning: MiniCartTickerWarningUiModel? = null
            var tickerWarningIndex = -1
            visitables.forEachIndexed { index, visitable ->
                if (visitable is MiniCartTickerWarningUiModel) {
                    tickerWarning = visitable
                    tickerWarningIndex = index
                    return@forEachIndexed
                }
            }

            val warningWording = miniCartUiModel.value?.maximumShippingWeightErrorMessage ?: ""
            val overWeight = (totalWeight - maxWeight) / 1000.0f
            if (tickerWarning == null) {
                tickerWarning = miniCartListViewHolderMapper.mapTickerWarningUiModel(overWeight, warningWording)
                tickerWarning?.let {
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
        visitables.forEachIndexed { index, visitable ->
            if (visitable is MiniCartTickerWarningUiModel) {
                tmpIndex = index
                return@forEachIndexed
            }
        }

        if (tmpIndex != -1) {
            visitables.removeAt(tmpIndex)
        }
    }

    fun handleUnavailableItemsAccordion() {
        val visitables = miniCartUiModel.value?.visitables?.toMutableList() ?: mutableListOf()
        var accordionUiModel: MiniCartAccordionUiModel? = null
        var indexAccordionUiModel: Int = -1
        visitables.forEachIndexed { index, visitable ->
            if (visitable is MiniCartAccordionUiModel) {
                accordionUiModel = visitable
                indexAccordionUiModel = index
                return@forEachIndexed
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
        val tmpUnavailableProducts = mutableListOf<MiniCartProductUiModel>()
        visitables.forEachIndexed { index, visitable ->
            if (visitable is MiniCartProductUiModel && visitable.isProductDisabled) {
                tmpUnavailableProducts.add(visitable)
            }
        }

        if (tmpUnavailableProducts.size > 1) {
            val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
                isCollapsed = !isCollapsed
            }
            visitables[indexAccordionUiModel] = updatedAccordionUiModel

            val hiddenUnavailableItems = tmpUnavailableProducts.takeLast(tmpUnavailableProducts.size - 1)
            tmpHiddenUnavailableItems.clear()
            tmpHiddenUnavailableItems.addAll(hiddenUnavailableItems)
            visitables.removeAll(hiddenUnavailableItems)

            miniCartUiModel.value?.visitables = visitables
            _miniCartUiModel.value = miniCartUiModel.value
        }
    }

    private fun expandUnavailableItems(visitables: MutableList<Visitable<*>>, accordionUiModel: MiniCartAccordionUiModel, indexAccordionUiModel: Int) {
        val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
            isCollapsed = !isCollapsed
        }
        visitables[indexAccordionUiModel] = updatedAccordionUiModel

        visitables.addAll(indexAccordionUiModel - 1, tmpHiddenUnavailableItems)
        tmpHiddenUnavailableItems.clear()

        miniCartUiModel.value?.visitables = visitables
        _miniCartUiModel.value = miniCartUiModel.value
    }
}