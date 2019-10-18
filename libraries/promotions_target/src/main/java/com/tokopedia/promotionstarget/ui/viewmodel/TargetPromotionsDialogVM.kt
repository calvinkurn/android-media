package com.tokopedia.promotionstarget.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.usecase.ClaimPopGratificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

class TargetPromotionsDialogVM @Inject constructor(@Named("Main")
                                                   val dispatcher: CoroutineDispatcher,
                                                   val claimPopGratificationUseCase: ClaimPopGratificationUseCase,
                                                   val autoApplyUseCase: AutoApplyUseCase
) : BaseViewModel(dispatcher) {

    val couponClaimLiveData: MutableLiveData<Result<ClaimPopGratificationResponse>> = MutableLiveData()
    val autoApplyLiveData: MutableLiveData<Result<AutoApplyResponse>> = MutableLiveData()
    private val HTTP_CODE_OK = "200"

    fun claimCoupon(campaignSlug: String, page: String) {

        launchCatchError(block = {
            val data = claimPopGratificationUseCase.let {
                it.getResponse(it.getQueryParams(campaignSlug, page))
            }
            if (data.popGratificationClaim?.resultStatus?.code == HTTP_CODE_OK) {
                couponClaimLiveData.value = Success(data)
            } else {
                couponClaimLiveData.value = Fail(Throwable("Unknown Exception"))
            }

        }, onError = {
            //todo check for no internet
            couponClaimLiveData.value = Fail(it)
        })
    }


    fun autoApply(code: String) {
        launchCatchError(block = {
            delay(5000L)
            val response = autoApplyUseCase.getResponse(autoApplyUseCase.getQueryParams(code))
            autoApplyLiveData.value = Success(response)
        }, onError = {
            autoApplyLiveData.value = Fail(it)
        })
    }
}