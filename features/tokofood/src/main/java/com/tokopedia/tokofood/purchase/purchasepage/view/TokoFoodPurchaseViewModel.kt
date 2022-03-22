package com.tokopedia.tokofood.purchase.purchasepage.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.purchase.purchasepage.view.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseAccordionUiModel
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseDividerUiModel
import javax.inject.Inject

class TokoFoodPurchaseViewModel @Inject constructor(val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val _visitables = MutableLiveData<List<Visitable<*>>>()
    val visitables: LiveData<List<Visitable<*>>>
        get() = _visitables

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
        tmpData.add(TokoFoodPurchaseDividerUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAddressUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapShippingUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(false))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(false))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(false))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(false))
        tmpData.add(TokoFoodPurchaseDividerUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(true))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUnavailableReasonUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(true))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(true))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(true))
        tmpData.add(TokoFoodPurchaseDividerUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAccordionUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapPromoUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapSummaryTransactionUiModel())
        tmpData.add(TokoFoodPurchaseDividerUiModel())
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapTotalAmountUiModel())
        _visitables.value = tmpData
    }
}