package com.tokopedia.buy_more_get_more.presentation.olp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.domain.entity.OfferInfoForBuyer
import com.tokopedia.buy_more_get_more.domain.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

class OfferLandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase
) : BaseViewModel(dispatchers.main) {

    private val _offeringInfo = MutableLiveData<OfferInfoForBuyer>()
    val offeringInfo: LiveData<OfferInfoForBuyer>
        get() = _offeringInfo

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun getOfferingIndo(offerIds: List<Int>, shopId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferInfoForBuyerUseCase.Param(
                    offerIds = offerIds,
                    shopId = shopId.toIntOrZero()
                )
                _offeringInfo.postValue(getOfferInfoForBuyerUseCase.execute(param))
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}
