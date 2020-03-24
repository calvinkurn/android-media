package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapCrackUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapHomeUseCase
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class GiftBoxTapTapViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher,
                                                 val crackUseCase: GiftBoxTapTapCrackUseCase,
                                                 val homeUseCase: GiftBoxTapTapHomeUseCase
) : BaseViewModel(workerDispatcher) {

    @Volatile
    var tokenId: String = ""
    @Volatile
    var campaignId: String = ""

    val giftHomeLiveData: MutableLiveData<LiveDataResult<TapTapBaseEntity>> = MutableLiveData()
    val giftCrackLiveData: MutableLiveData<LiveDataResult<ResponseCrackResultEntity>> = MutableLiveData()

    fun getGiftBoxHome() {
        giftHomeLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            val response = homeUseCase.getResponse(HashMap())
            giftHomeLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            giftHomeLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun crackGiftBox() {
        launchCatchError(block = {
            val response = crackUseCase.getResponse(crackUseCase.getQueryParams(tokenId, campaignId))
            giftCrackLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            giftCrackLiveData.postValue(LiveDataResult.error(it))
        })
    }

}