package com.tokopedia.promotionstarget.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.usecase.ClaimPopGratificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class TargetPromotionsDialogVM @Inject constructor(@Named("Main")
                                                   val dispatcher: CoroutineDispatcher,
                                                   val claimPopGratificationUseCase: ClaimPopGratificationUseCase
) : BaseViewModel(dispatcher) {

    val liveData: MutableLiveData<Result<ClaimPopGratificationResponse>> = MutableLiveData()

    fun claimCoupon(campaignSlug: String="", page: String="") {

        launchCatchError(block = {
            val data = claimPopGratificationUseCase.let {
                it.getResponse(it.getQueryParams(campaignSlug, page))
            }

            liveData.value = Success(data)

        }, onError = {
            //todo check for no internet
            liveData.value = Fail(it)
        })
    }
}