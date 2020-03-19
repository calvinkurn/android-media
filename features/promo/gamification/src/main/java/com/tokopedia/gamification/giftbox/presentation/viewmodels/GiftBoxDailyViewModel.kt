package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyUseCase
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyViewModel @Inject constructor(@Named(MAIN) uiDispatcher: CoroutineDispatcher,
                                                @Named(IO) workerDispatcher: CoroutineDispatcher,
                                                val giftBoxDailyUseCase: GiftBoxDailyUseCase,
                                                val giftBoxDailyRewardUseCase: GiftBoxDailyRewardUseCase)
    : BaseViewModel(workerDispatcher) {

    //todo Rahul these below values must not be hardcoded
    var campaignSlug: String = "qr2020"
    var pageName: String = "giftbox"
    var uniqueCode: String = ""

    val giftBoxLiveData: MutableLiveData<LiveDataResult<GiftBoxEntity>> = MutableLiveData()
    val rewardLiveData: MutableLiveData<LiveDataResult<GiftBoxRewardEntity>> = MutableLiveData()

    fun getGiftBox() {
        giftBoxLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            val params = giftBoxDailyUseCase.getRequestParams(campaignSlug, pageName)
//            giftBoxLiveData.postValue(LiveDataResult.success(giftBoxDailyUseCase.getResponse(params)))
            giftBoxLiveData.postValue(LiveDataResult.success(giftBoxDailyUseCase.getFakeResponse()))
        }, onError = {
            giftBoxLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getRewards() {
        launchCatchError(block = {
            val params = giftBoxDailyRewardUseCase.getRequestParams(campaignSlug, uniqueCode)
            rewardLiveData.postValue(LiveDataResult.success(giftBoxDailyRewardUseCase.getResponse(params)))
        }, onError = {
            rewardLiveData.postValue(LiveDataResult.error(it))
        })
    }
}