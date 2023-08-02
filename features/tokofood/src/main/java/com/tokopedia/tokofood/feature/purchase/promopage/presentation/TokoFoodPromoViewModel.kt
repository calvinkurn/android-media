package com.tokopedia.tokofood.feature.purchase.promopage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.mapper.TokoFoodPromoUiModelMapper
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoFragmentUiModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class TokoFoodPromoViewModel @Inject constructor(
    private val promoListTokoFoodUseCase: Lazy<PromoListTokoFoodUseCase>,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

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

    private val _changeRestrictionMessage = MutableLiveData<String>()

    fun loadData(source: String,
                 merchantId: String,
                 cartList: List<String>) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                promoListTokoFoodUseCase.get().execute(source, merchantId, cartList)
            }.let {
                val customResponse = it.getTokofoodBusinessData().customResponse
                when {
                    customResponse.errorPage.isShowErrorPage -> {
                        _uiEvent.value = UiEvent(
                            state = UiEvent.EVENT_ERROR_PAGE_PROMO_PAGE,
                            data = customResponse.errorPage
                        )
                    }
                    customResponse.availableSection.subSection.coupons.isNotEmpty() || customResponse.unavailableSection.subSection.coupons.isNotEmpty() -> {
                        _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE)
                        _fragmentUiModel.value =
                            TokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(customResponse)
                        _visitables.value =
                            TokoFoodPromoUiModelMapper.mapResponseDataToVisitables(customResponse)
                        customResponse.changeRestrictionMessage.takeIf { message -> message.isNotEmpty() }
                            ?.let { message ->
                                _changeRestrictionMessage.value = message
                            }
                    }
                    else -> {
                        _uiEvent.value = UiEvent(
                            state = UiEvent.EVENT_NO_COUPON,
                            data = customResponse.emptyState
                        )
                    }
                }
            }
        }, onError = {
            _uiEvent.value = UiEvent(
                state = UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE,
                throwable = it
            )
        })
    }

    fun showChangeRestrictionMessage() {
        _uiEvent.value = UiEvent(
            state = UiEvent.EVENT_SHOW_TOASTER,
            data = _changeRestrictionMessage.value
        )
    }

}
