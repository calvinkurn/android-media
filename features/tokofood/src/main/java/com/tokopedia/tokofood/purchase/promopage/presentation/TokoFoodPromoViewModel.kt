package com.tokopedia.tokofood.purchase.promopage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TabUiModel
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoFragmentUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.mapper.TokoFoodPromoUiModelMapper
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoFoodPromoViewModel @Inject constructor(val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val _uiEvent = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent>
        get() = _uiEvent

    private val _fragmentUiModel = MutableLiveData<TokoFoodPromoFragmentUiModel>()
    val fragmentUiModel: LiveData<TokoFoodPromoFragmentUiModel>
        get() = _fragmentUiModel

    // List of recyclerview items
    private val _visitables = MutableLiveData<MutableList<Visitable<*>>>()
    val visitables: LiveData<MutableList<Visitable<*>>>
        get() = _visitables

    private fun getVisitablesValue(): MutableList<Visitable<*>> {
        return visitables.value ?: mutableListOf()
    }

    fun loadData() {
        // Todo : Load from API, if success then map to UiModel, if error show global error
        launch {
            delay(3000) // Simulate hit API
            val isSuccess = true
            if (isSuccess) {
                _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE)
                _fragmentUiModel.value = TokoFoodPromoFragmentUiModel(
                        promoAmount = 100000,
                        promoCount = 1,
                        tabs = listOf(
                                TabUiModel(
                                        id = "0",
                                        title = "Kupon Otomatis"
                                ),
                                TabUiModel(
                                        id = "1",
                                        title = "Belum Bisa Dipakai"
                                )
                        )
                )
                constructRecycleViewItem()
            } else {
                // Todo : Set throwable from network
                _uiEvent.value = UiEvent(state = UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE)
            }
        }
    }

    private fun constructRecycleViewItem() {
        val tmpData = mutableListOf<Visitable<*>>()
        // Todo : map data
        tmpData.add(TokoFoodPromoUiModelMapper.mapHeaderUiModel(false))
        tmpData.add(TokoFoodPromoUiModelMapper.mapTickerUiModel())
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(false))
        tmpData.add(TokoFoodPromoUiModelMapper.mapEligibilityUiModel())
        tmpData.add(TokoFoodPromoUiModelMapper.mapHeaderUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        tmpData.add(TokoFoodPromoUiModelMapper.mapPromoItemUiModel(true))
        _visitables.value = tmpData
    }

}