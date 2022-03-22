package com.tokopedia.tokofood.purchase.purchasepage.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.purchase.purchasepage.view.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseAccordionUiModel
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseDividerUiModel
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductUiModel
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductUnavailableReasonUiModel
import javax.inject.Inject

class TokoFoodPurchaseViewModel @Inject constructor(val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val _visitables = MutableLiveData<MutableList<Visitable<*>>>()
    val visitables: LiveData<MutableList<Visitable<*>>>
        get() = _visitables

    private var tmpCollapsedUnavailableItems = mutableListOf<Visitable<*>>()

    private fun getAccordionUiModel(): Pair<Int, TokoFoodPurchaseAccordionUiModel>? {
        val dataList = visitables.value ?: emptyList()
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseAccordionUiModel) {
                return Pair(index, data)
            }
        }
        return null
    }

    private fun getUnavailableReasonUiModel(): Pair<Int, TokoFoodPurchaseProductUnavailableReasonUiModel>? {
        val dataList = visitables.value ?: emptyList()
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseProductUnavailableReasonUiModel) {
                return Pair(index, data)
            }
        }
        return null
    }

    fun getPreviousItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        val list = visitables.value ?: emptyList()
        var from = currentIndex - count
        if (from < 0) {
            from = 0
        }
        return list.subList(from, currentIndex)
    }

    fun loadData() {
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

    fun toggleUnavailableProductsAccordion() {
        val accordionData = getAccordionUiModel()
        accordionData?.let { mAccordionData ->
            val newAccordionUiModel = accordionData.second.copy()

            if (mAccordionData.second.isCollapsed) {
                newAccordionUiModel.isCollapsed = false
                visitables.value?.set(mAccordionData.first, newAccordionUiModel)
                val index = mAccordionData.first - 1
                visitables.value?.addAll(index, tmpCollapsedUnavailableItems)
            } else {
                newAccordionUiModel.isCollapsed = true
                visitables.value?.set(mAccordionData.first, newAccordionUiModel)
                val unavailableReasonData = getUnavailableReasonUiModel()
                unavailableReasonData?.let { mUnavailableReasonData ->
                    val from = mUnavailableReasonData.first + 2
                    val to = mAccordionData.first - 1
                    tmpCollapsedUnavailableItems.clear()
                    tmpCollapsedUnavailableItems = visitables.value?.subList(from, to)?.toMutableList()
                            ?: mutableListOf()
                }
                visitables.value?.removeAll(tmpCollapsedUnavailableItems)
            }

            _visitables.value = visitables.value
        }
    }
}