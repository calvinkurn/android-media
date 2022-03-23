package com.tokopedia.tokofood.purchase.purchasepage.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.purchase.purchasepage.view.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.*
import javax.inject.Inject

class TokoFoodPurchaseViewModel @Inject constructor(val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    // List of recyclerview items
    private val _visitables = MutableLiveData<MutableList<Visitable<*>>>()
    val visitables: LiveData<MutableList<Visitable<*>>>
        get() = _visitables

    // Temporary field to store collapsed unavailable products
    private var tmpCollapsedUnavailableItems = mutableListOf<Visitable<*>>()

    private fun getVisitablesValue(): MutableList<Visitable<*>> {
        return visitables.value ?: mutableListOf()
    }

    private fun getProductByProductId(productId: String): TokoFoodPurchaseProductUiModel? {
        val dataList = getVisitablesValue()
        loop@ for (data in dataList) {
            when {
                data is TokoFoodPurchaseProductUiModel && data.id == productId -> {
                    return data
                }
                data is TokoFoodPurchaseAccordionUiModel || data is TokoFoodPurchasePromoUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    private fun getAccordionUiModel(): Pair<Int, TokoFoodPurchaseAccordionUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            when (data) {
                is TokoFoodPurchaseAccordionUiModel -> {
                    return Pair(index, data)
                }
                is TokoFoodPurchasePromoUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    private fun getUnavailableReasonUiModel(): Pair<Int, TokoFoodPurchaseProductUnavailableReasonUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseProductUnavailableReasonUiModel) {
                return Pair(index, data)
            }
        }
        return null
    }

    private fun getAllUnavailableProducts(): Pair<Int, List<TokoFoodPurchaseProductUiModel>> {
        val dataList = getVisitablesValue()
        var firstItemIndex = -1
        val unavailableProducts = mutableListOf<TokoFoodPurchaseProductUiModel>()
        loop@ for ((index, data) in dataList.withIndex()) {
            when {
                data is TokoFoodPurchaseProductUiModel && data.isDisabled -> {
                    if (firstItemIndex == -1) firstItemIndex = index
                    unavailableProducts.add(data)
                }
                data is TokoFoodPurchaseAccordionUiModel || data is TokoFoodPurchasePromoUiModel -> {
                    break@loop
                }
            }
        }
        return Pair(firstItemIndex, unavailableProducts)
    }

    fun getPreviousItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        val dataList = getVisitablesValue()
        var from = currentIndex - count
        if (from < 0) {
            from = 0
        }
        return dataList.subList(from, currentIndex)
    }

    fun loadData() {
        // Todo : Load from API --> map to UiModel
        val tmpData = mutableListOf<Visitable<*>>()
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapGeneralTickerUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "1"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAddressUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapShippingUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "2"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(false))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(false, "1"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(false, "2"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(false, "3"))
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "3"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(true))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUnavailableReasonUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(true, "4"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(true, "5"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(true, "6"))
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "4"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAccordionUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "5"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapPromoUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "6"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapSummaryTransactionUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "7"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapTotalAmountUiModel())
        _visitables.value = tmpData
    }

    fun calculateTotal() {

    }

    private fun deleteProducts(visitables: List<Visitable<*>>) {
        val dataList = getVisitablesValue()
        dataList.removeAll(visitables)
        _visitables.value = dataList
    }

    fun deleteProduct(productId: String) {
        // Todo : hit API to remove product, once it's success, perform below code to remove local data
        val toBeDeletedProduct = getProductByProductId(productId)
        if (toBeDeletedProduct != null) {
            val dataList = getVisitablesValue()
            dataList.remove(toBeDeletedProduct)
            _visitables.value = dataList

            if (!hasRemainingProduct()) {
                // Todo: navigate to merchant page
            }
        }
    }

    private fun hasRemainingProduct(): Boolean {
        val dataList = getVisitablesValue()
        loop@ for (data in dataList) {
            when (data) {
                is TokoFoodPurchaseProductUiModel -> {
                    return true
                }
                is TokoFoodPurchasePromoUiModel -> {
                    return false
                }
            }
        }
        return false
    }

    fun bulkDeleteUnavailableProducts() {
        // Todo : hit API to remove product, once it's success, perform below code to remove local data
        val dataList = getVisitablesValue()
        val unavailableSectionItems = mutableListOf<Visitable<*>>()

        val unavailableProducts = getAllUnavailableProducts()
        val indexOfUnavailableHeaderDivider = unavailableProducts.first - 3
        val indexOfFirstUnavailableProduct = unavailableProducts.first
        val unavailableSectionDividerHeaderAndReason = dataList.subList(indexOfUnavailableHeaderDivider, indexOfFirstUnavailableProduct)
        unavailableSectionItems.addAll(unavailableSectionDividerHeaderAndReason)
        unavailableSectionItems.addAll(unavailableProducts.second)
        val accordionUiModel = getAccordionUiModel()
        accordionUiModel?.let {
            val accordionDivider = dataList.get(accordionUiModel.first - 1)
            unavailableSectionItems.add(accordionDivider)
            unavailableSectionItems.add(it.second)
        }
        tmpCollapsedUnavailableItems.clear()
        deleteProducts(unavailableSectionItems)
    }

    fun toggleUnavailableProductsAccordion() {
        val dataList = getVisitablesValue()
        val accordionData = getAccordionUiModel()
        accordionData?.let { mAccordionData ->
            val newAccordionUiModel = accordionData.second.copy()

            if (mAccordionData.second.isCollapsed) {
                expandUnavailableProducts(newAccordionUiModel, dataList, mAccordionData)
            } else {
                collapseUnavailableProducts(newAccordionUiModel, dataList, mAccordionData)
            }

            _visitables.value = dataList
        }
    }

    private fun collapseUnavailableProducts(newAccordionUiModel: TokoFoodPurchaseAccordionUiModel,
                                            dataList: MutableList<Visitable<*>>,
                                            mAccordionData: Pair<Int, TokoFoodPurchaseAccordionUiModel>) {
        newAccordionUiModel.isCollapsed = true
        dataList[mAccordionData.first] = newAccordionUiModel
        val unavailableReasonData = getUnavailableReasonUiModel()
        unavailableReasonData?.let { mUnavailableReasonData ->
            val from = mUnavailableReasonData.first + 2
            val to = mAccordionData.first - 1
            tmpCollapsedUnavailableItems.clear()
            tmpCollapsedUnavailableItems = dataList.subList(from, to).toMutableList()
        }
        dataList.removeAll(tmpCollapsedUnavailableItems)
    }

    private fun expandUnavailableProducts(newAccordionUiModel: TokoFoodPurchaseAccordionUiModel,
                                          dataList: MutableList<Visitable<*>>,
                                          mAccordionData: Pair<Int, TokoFoodPurchaseAccordionUiModel>) {
        newAccordionUiModel.isCollapsed = false
        dataList[mAccordionData.first] = newAccordionUiModel
        val index = mAccordionData.first - 1
        dataList.addAll(index, tmpCollapsedUnavailableItems)
    }
}