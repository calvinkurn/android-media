package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.domain.usecase.ClaimPopGratificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.promotionstarget.presentation.SingleLiveEvent
import com.tokopedia.promotionstarget.presentation.TargetedPromotionAnalytics
import com.tokopedia.promotionstarget.presentation.launchCatchError
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.promotionstarget.presentation.ui.CustomToast
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class TargetPromotionsDialogVM @Inject constructor(@Named("Main")
                                                   val uiDispatcher: CoroutineDispatcher,
                                                   @Named("IO")
                                                   val workerDispatcher: CoroutineDispatcher,
                                                   val autoApplyUseCase: AutoApplyUseCase,
                                                   val claimPopGratificationUseCase: ClaimPopGratificationUseCase,
                                                   val couponDetailUseCase: GetCouponDetailUseCase,
                                                   val app: Application
) : BaseAndroidViewModel(uiDispatcher, app) {

    val autoApplyLiveData: SingleLiveEvent<LiveDataResult<AutoApplyResponse>> = SingleLiveEvent()
    val claimApiLiveData = MutableLiveData<LiveDataResult<Pair<ClaimPopGratificationResponse, GetCouponDetailResponse>>>()
    var gratificationData:GratificationData?=null

    fun claimGratification(campaignSlug: String, page: String, benefitIds: List<Int?>?) {
        claimApiLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            val response = withContext(workerDispatcher) {
                val claimResponse = claimPopGratificationUseCase.getResponse(claimPopGratificationUseCase.getQueryParams(ClaimPayload(campaignSlug, page)))
                val couponDetail = composeApi(benefitIds)
                return@withContext Pair(claimResponse, couponDetail)
            }
            claimApiLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            claimApiLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun autoApply(code: String) {
        launchCatchError(block = {
            val response = withContext(workerDispatcher) {
                val map = autoApplyUseCase.getQueryParams(code)
                autoApplyUseCase.getResponse(map)
            }
            showToast(LiveDataResult.success(response))
            autoApplyLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            autoApplyLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun showToast(it: LiveDataResult<AutoApplyResponse>) {
        when (it.status) {
            LiveDataResult.STATUS.SUCCESS -> {
                performShowToast(it)
            }
        }
    }

    fun performShowToast(it:LiveDataResult<AutoApplyResponse>){
        val messageList = it.data?.tokopointsSetAutoApply?.resultStatus?.message
        if (messageList != null && messageList.isNotEmpty()) {
            CustomToast.show(app, messageList[0].toString())
            val userSession = UserSession(app)
            val label = "${gratificationData?.popSlug} - ${messageList[0].toString()}"
            TargetedPromotionAnalytics.claimSucceedPopup(label, userSession.userId)
        }
    }

    suspend fun composeApi(listIds: List<Int?>?): GetCouponDetailResponse {
        val idsString = arrayListOf<String>()
        listIds?.let {
            it.forEach { item ->
                item?.let { nonNullItem -> idsString.add(nonNullItem.toString()) }

            }
        }
        return getCatalogDetail(idsString)
    }


    private suspend fun getCatalogDetail(ids: List<String>): GetCouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }
}