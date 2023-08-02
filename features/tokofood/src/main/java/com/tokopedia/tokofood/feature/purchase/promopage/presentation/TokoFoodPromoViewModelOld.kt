package com.tokopedia.tokofood.feature.purchase.promopage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCaseOld
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.mapper.TokoFoodPromoUiModelMapperOld
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoFragmentUiModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class TokoFoodPromoViewModelOld @Inject constructor(
    private val promoListTokoFoodUseCase: Lazy<PromoListTokoFoodUseCaseOld>,
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
                 merchantId: String) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                promoListTokoFoodUseCase.get().execute(source, merchantId)
            }.let {
                if (it.isSuccess()) {
                    when {
                        it.data.errorPage.isShowErrorPage -> {
                            _uiEvent.value = UiEvent(
                                state = UiEvent.EVENT_ERROR_PAGE_PROMO_PAGE,
                                data = it.data.errorPage
                            )
                        }
                        it.data.availableSection.subSection.coupons.isNotEmpty() || it.data.unavailableSection.subSection.coupons.isNotEmpty() -> {
                            _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE)
                            _fragmentUiModel.value =
                                TokoFoodPromoUiModelMapperOld.mapResponseDataToFragmentUiModel(it.data)
                            _visitables.value =
                                TokoFoodPromoUiModelMapperOld.mapResponseDataToVisitables(it.data)
                            it.data.changeRestrictionMessage.takeIf { message -> message.isNotEmpty() }
                                ?.let { message ->
                                    _changeRestrictionMessage.value = message
                                }
                        }
                        else -> {
                            _uiEvent.value = UiEvent(
                                state = UiEvent.EVENT_NO_COUPON,
                                data = it.data.emptyState
                            )
                        }
                    }
                } else {
                    _uiEvent.value = UiEvent(
                        state = UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE,
                        throwable = MessageErrorException(it.message)
                    )
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
