package com.tokopedia.buy_more_get_more.olp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.olp.data.request.GetOfferingInfoForBuyerRequestParam
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class OfferLandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase
) : BaseViewModel(dispatchers.main) {

    private val _offeringInfo = MutableLiveData<OfferInfoForBuyerUiModel>()
    val offeringInfo: LiveData<OfferInfoForBuyerUiModel>
        get() = _offeringInfo

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun getOfferingIndo(offerIds: List<Int>, shopId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferingInfoForBuyerRequestParam()
                _offeringInfo.postValue(getOfferInfoForBuyerUseCase.execute(param))
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}
