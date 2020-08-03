package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import com.tokopedia.gamification.giftbox.data.entities.*
import com.tokopedia.gamification.giftbox.domain.*
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyViewModel @Inject constructor(@Named(MAIN) uiDispatcher: CoroutineDispatcher,
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
    val autoApplyLiveData: MutableLiveData<LiveDataResult<AutoApplyResponse>> = MutableLiveData()

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
                if (!it.referenceID.isNullOrEmpty()) {
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
            autoApplyLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            autoApplyLiveData.postValue(LiveDataResult.error(it))
        })
    }
}