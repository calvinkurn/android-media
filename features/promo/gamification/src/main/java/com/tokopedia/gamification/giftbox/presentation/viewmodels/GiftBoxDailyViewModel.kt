package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyUseCase
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
                                                val couponDetailUseCase: CouponDetailUseCase)
    : BaseViewModel(workerDispatcher) {

    //todo Rahul these below values must not be hardcoded
    var campaignSlug: String = "active"
    var pageName: String = "daily"
    var uniqueCode: String = ""

    val giftBoxLiveData: MutableLiveData<LiveDataResult<GiftBoxEntity>> = MutableLiveData()
    val rewardLiveData: MutableLiveData<LiveDataResult<GiftBoxRewardEntity>> = MutableLiveData()

    var rewardJob: Job? = null

    fun getGiftBox() {
        giftBoxLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            delay(1000L)
            val params = giftBoxDailyUseCase.getRequestParams(campaignSlug, pageName)
//            val r = giftBoxDailyUseCase.getResponse(params)
//            giftBoxLiveData.postValue(LiveDataResult.success(giftBoxDailyUseCase.getResponse(params)))
            giftBoxLiveData.postValue(LiveDataResult.success(giftBoxDailyUseCase.getFakeResponseActive()))
        }, onError = {
            giftBoxLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getRewards() {
        if (rewardJob == null || rewardJob!!.isCompleted) {
            rewardJob = launchCatchError(block = {
                val params = giftBoxDailyRewardUseCase.getRequestParams(campaignSlug, uniqueCode)
//            rewardLiveData.postValue(LiveDataResult.success(giftBoxDailyRewardUseCase.getResponse(params)))

                val response = giftBoxDailyRewardUseCase.getCouponsWithOvoPoints()
                val couponDetail = composeApi(response)
                response.couponDetailResponse = couponDetail
                rewardLiveData.postValue(LiveDataResult.success(response))

            }, onError = {
                rewardLiveData.postValue(LiveDataResult.error(it))
            })
        }
    }

    suspend fun composeApi(giftBoxRewardEntity: GiftBoxRewardEntity): CouponDetailResponse {
        val ids = mapperGratificationResponseToCouponIds(giftBoxRewardEntity)
        return getCatalogDetail(ids)
    }

    private fun mapperGratificationResponseToCouponIds(response: GiftBoxRewardEntity): List<String> {
        var ids = arrayListOf<String>()
        response.crackResult.benefits?.forEach {
            if (it.referenceID!=null){
                ids.add(it.referenceID.toString())
            }
        }
        return ids
    }

    private suspend fun getCatalogDetail(ids: List<String>): CouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }
}