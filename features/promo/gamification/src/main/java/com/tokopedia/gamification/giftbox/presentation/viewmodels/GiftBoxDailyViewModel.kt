package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import com.tokopedia.gamification.giftbox.data.entities.*
import com.tokopedia.gamification.giftbox.domain.*
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyViewModel @Inject constructor(@Named(MAIN) val ui: CoroutineDispatcher,
                                                @Named(IO) workerDispatcher: CoroutineDispatcher,
                                                val giftBoxDailyUseCase: GiftBoxDailyUseCase,
                                                val giftBoxDailyRewardUseCase: GiftBoxDailyRewardUseCase,
                                                val couponDetailUseCase: CouponDetailUseCase,
                                                val remindMeUseCase: RemindMeUseCase,
                                                val autoApplyUseCase: AutoApplyUseCase)
    : BaseViewModel(workerDispatcher) {

    @Volatile
    var campaignSlug: String? = ""
    var pageName: String = "giftbox"
    var uniqueCode: String = ""
    var about: String = "dailybox"

    val giftBoxLiveData: MutableLiveData<LiveDataResult<Pair<GiftBoxEntity, RemindMeCheckEntity>>> = MutableLiveData()
    val rewardLiveData: MutableLiveData<LiveDataResult<GiftBoxRewardEntity>> = MutableLiveData()
    val reminderSetLiveData: MutableLiveData<LiveDataResult<RemindMeEntity>> = MutableLiveData()
    var autoApplycallback: AutoApplyCallback?=null
    var rewardJob: Job? = null
    var remindMeJob: Job? = null

    fun getGiftBox() {
        giftBoxLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            val params = giftBoxDailyUseCase.getRequestParams(pageName)
            val response = giftBoxDailyUseCase.getResponse(params)
            campaignSlug = response?.gamiLuckyHome.tokensUser?.campaignSlug
            val remindMeCheckEntity = remindMeUseCase.getRemindMeCheckResponse(remindMeUseCase.getRequestParams(about))

            giftBoxLiveData.postValue(LiveDataResult.success(Pair(response, remindMeCheckEntity)))

        }, onError = {
            giftBoxLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getRewards() {
        if (rewardJob == null || rewardJob!!.isCompleted && campaignSlug != null) {
            rewardJob = launchCatchError(block = {
                val response: GiftBoxRewardEntity
                val params = giftBoxDailyRewardUseCase.getRequestParams(campaignSlug!!, uniqueCode)
                response = giftBoxDailyRewardUseCase.getResponse(params)

                val couponDetail = composeApi(response)
                response.couponDetailResponse = couponDetail
                rewardLiveData.postValue(LiveDataResult.success(response))

            }, onError = {
                rewardLiveData.postValue(LiveDataResult.error(it))
            })
        }
    }

    fun setReminder() {
        if (remindMeJob == null || remindMeJob!!.isCompleted) {
            reminderSetLiveData.postValue(LiveDataResult.loading())
            remindMeJob = launchCatchError(block = {
                val response = remindMeUseCase.getRemindMeResponse(remindMeUseCase.getRequestParams(about))
                response.gameRemindMe.requestToSetReminder = true
                reminderSetLiveData.postValue(LiveDataResult.success(response))
            }, onError = {
                reminderSetLiveData.postValue(LiveDataResult.error(it))
            })
        }
    }

    fun unSetReminder() {
        if (remindMeJob == null || remindMeJob!!.isCompleted) {
            reminderSetLiveData.postValue(LiveDataResult.loading())
            remindMeJob = launchCatchError(block = {
                val response = remindMeUseCase.getUnSetRemindMeResponse(remindMeUseCase.getRequestParams(about))
                response.gameRemindMe.requestToSetReminder = false
                reminderSetLiveData.postValue(LiveDataResult.success(response))
            }, onError = {
                reminderSetLiveData.postValue(LiveDataResult.error(it))
            })
        }
    }

    suspend fun composeApi(giftBoxRewardEntity: GiftBoxRewardEntity): CouponDetailResponse? {

        suspend fun getCatalogDetail(ids: List<String>): CouponDetailResponse {
            return couponDetailUseCase.getResponse(ids)
        }

        fun mapperGratificationResponseToCouponIds(response: GiftBoxRewardEntity): List<String> {
            var ids = arrayListOf<String>()
            response.gamiCrack.benefits?.forEach {
                if (!it.referenceID.isNullOrEmpty() && it.displayType == DisplayType.CATALOG) {
                    ids.add(it.referenceID)
                }
            }
            return ids
        }

        val ids = mapperGratificationResponseToCouponIds(giftBoxRewardEntity)
        if (ids.isEmpty()) {
            return null
        }
        return getCatalogDetail(ids)
    }

    fun autoApply(code: String) {
        launchCatchError(block = {
            val map = autoApplyUseCase.getQueryParams(code)
            var response: AutoApplyResponse
            response = autoApplyUseCase.getResponse(map)
            withContext(ui) {
                autoApplycallback?.success(response)
            }
        }, onError = {
            Timber.e(it)
        })
    }
}

interface AutoApplyCallback {
    fun success(response: AutoApplyResponse?)
}