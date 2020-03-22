package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import com.tokopedia.gamification.giftbox.data.entities.*
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyUseCase
import com.tokopedia.gamification.giftbox.domain.RemindMeUseCase
import com.tokopedia.gamification.giftbox.presentation.activities.GiftLauncherActivity
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyViewModel @Inject constructor(@Named(MAIN) uiDispatcher: CoroutineDispatcher,
                                                @Named(IO) workerDispatcher: CoroutineDispatcher,
                                                val giftBoxDailyUseCase: GiftBoxDailyUseCase,
                                                val giftBoxDailyRewardUseCase: GiftBoxDailyRewardUseCase,
                                                val couponDetailUseCase: CouponDetailUseCase,
                                                val remindMeUseCase: RemindMeUseCase)
    : BaseViewModel(workerDispatcher) {

    //todo Rahul these below values must not be hardcoded
    @Volatile
    var campaignSlug: String? = ""
    var pageName: String = "giftbox"
    var uniqueCode: String = ""

    val giftBoxLiveData: MutableLiveData<LiveDataResult<GiftBoxEntity>> = MutableLiveData()
    val rewardLiveData: MutableLiveData<LiveDataResult<GiftBoxRewardEntity>> = MutableLiveData()
    val reminderLiveData: MutableLiveData<LiveDataResult<RemindMeEntity>> = MutableLiveData()
    val reminderCheckLiveData: MutableLiveData<LiveDataResult<RemindMeCheckEntity>> = MutableLiveData()

    var rewardJob: Job? = null
    var remindMeJob: Job? = null

    fun getGiftBox() {
        giftBoxLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            delay(1000L)
            val params = giftBoxDailyUseCase.getRequestParams(pageName)
            if (GiftLauncherActivity.iS_STAGING) {
                val response = giftBoxDailyUseCase.getResponse(params)
                campaignSlug = response?.gamiLuckyHome.tokensUser?.campaignSlug
                val remindMeCheckEntity = remindMeUseCase.getRemindMeCheckResponse(remindMeUseCase.getRequestParams(""))
                giftBoxLiveData.postValue(LiveDataResult.success(response))

                remindMeCheckEntity.reminder = response.gamiLuckyHome.reminder
                reminderCheckLiveData.postValue(LiveDataResult.success(remindMeCheckEntity))

            } else {
                val response = giftBoxDailyUseCase.getFakeResponseActive()
                giftBoxLiveData.postValue(LiveDataResult.success(response))
                val remindMeCheckEntity = remindMeUseCase.getRemindMeCheckResponseFake()
                remindMeCheckEntity.reminder = response.gamiLuckyHome.reminder
                reminderCheckLiveData.postValue(LiveDataResult.success(remindMeCheckEntity))
            }

        }, onError = {
            giftBoxLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getRewards() {
        if (rewardJob == null || rewardJob!!.isCompleted && campaignSlug != null) {
            rewardJob = launchCatchError(block = {
                val response: GiftBoxRewardEntity
                if (GiftLauncherActivity.iS_STAGING) {
                    val params = giftBoxDailyRewardUseCase.getRequestParams(campaignSlug!!, uniqueCode)
                    response = giftBoxDailyRewardUseCase.getResponse(params)
                } else {
                    response = giftBoxDailyRewardUseCase.getCouponsWithOvoPoints()
                }
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
            reminderLiveData.postValue(LiveDataResult.loading())
            remindMeJob = launchCatchError(block = {
                val response = remindMeUseCase.getRemindMeResponse(remindMeUseCase.getRequestParams(""))
                reminderLiveData.postValue(LiveDataResult.success(response))
            }, onError = {
                reminderLiveData.postValue(LiveDataResult.error(it))
            })
        }
    }

    suspend fun composeApi(giftBoxRewardEntity: GiftBoxRewardEntity): CouponDetailResponse? {
        val ids = mapperGratificationResponseToCouponIds(giftBoxRewardEntity)
        if (ids.isEmpty()) {
            return null
        }
        return getCatalogDetail(ids)
    }

    private fun mapperGratificationResponseToCouponIds(response: GiftBoxRewardEntity): List<String> {
        var ids = arrayListOf<String>()
        response.gamiCrack.benefits?.forEach {
            if (it.referenceID != null) {
                if (it.referenceID != 0) {
                    ids.add(it.referenceID.toString())
                }
            }
        }
        return ids
    }

    private suspend fun getCatalogDetail(ids: List<String>): CouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }
}