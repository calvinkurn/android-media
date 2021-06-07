package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.IO
import com.tokopedia.promotionstarget.data.di.MAIN
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.domain.usecase.ClaimPopGratificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.promotionstarget.presentation.TargetedPromotionAnalytics
import com.tokopedia.promotionstarget.presentation.launchCatchError
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.promotionstarget.presentation.ui.CustomToast
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class TargetPromotionsDialogViewModel @Inject constructor(@Named(MAIN)
                                                   val uiDispatcher: CoroutineDispatcher,
                                                          @Named(IO)
                                                   val workerDispatcher: CoroutineDispatcher,
                                                          val autoApplyUseCase: AutoApplyUseCase,
                                                          val claimPopGratificationUseCase: ClaimPopGratificationUseCase,
                                                          val couponDetailUseCase: GetCouponDetailUseCase,
                                                          val app: Application
) : BaseAndroidViewModel(uiDispatcher, app) {

    val autoApplyLiveData: SingleLiveEvent<LiveDataResult<AutoApplyResponse>> = SingleLiveEvent()
    val claimApiLiveData = MutableLiveData<LiveDataResult<Pair<ClaimPopGratificationResponse, GetCouponDetailResponse>>>()
    var gratificationData: GratificationData? = null

    fun claimGratification(campaignSlug: String, page: String, benefitIds: List<Int?>?) {
        claimApiLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            withContext(workerDispatcher){

                if (!benefitIds.isNullOrEmpty()) {
                    if (benefitIds[0] == 0) {
                        delay(300L) // to show loader for 300ms
                        claimApiLiveData.postValue(LiveDataResult.error(Exception("No benefits")))
                        return@withContext
                    }
                }
                val claimResponse = claimPopGratificationUseCase.getResponse(claimPopGratificationUseCase.getQueryParams(ClaimPayload(campaignSlug, page)))
                val couponDetail = composeApi(benefitIds)
                val response =  Pair(claimResponse, couponDetail)
                claimApiLiveData.postValue(LiveDataResult.success(response))
            }
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

    fun performShowToast(it: LiveDataResult<AutoApplyResponse>) {
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