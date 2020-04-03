package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
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
                                                 val homeUseCase: GiftBoxTapTapHomeUseCase,
                                                 val couponDetailUseCase: CouponDetailUseCase
) : BaseViewModel(workerDispatcher) {

    @Volatile
    var tokenId: String = ""
    @Volatile
    var campaignId: String = ""

    val giftHomeLiveData: MutableLiveData<LiveDataResult<TapTapBaseEntity>> = MutableLiveData()
    val giftCrackLiveData: MutableLiveData<LiveDataResult<Pair<CouponDetailResponse?, ResponseCrackResultEntity>>> = MutableLiveData()

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
            val couponDetail = composeApi(response)
            giftCrackLiveData.postValue(LiveDataResult.success(Pair(couponDetail, response)))
        }, onError = {
            giftCrackLiveData.postValue(LiveDataResult.error(it))
        })
    }

    suspend fun composeApi(entity: ResponseCrackResultEntity): CouponDetailResponse? {
        val ids = mapperGratificationResponseToCouponIds(entity)
        if (ids.isEmpty()) {
            return null
        }
        return getCatalogDetail(ids)
    }


    //todo Rahul later, after clearing doubts from backend
    private fun mapperGratificationResponseToCouponIds(response: ResponseCrackResultEntity): List<String> {
        var ids = arrayListOf<String>()
        response.crackResultEntity.benefits?.forEach {
            //            if (it.referenceID != null) {
//                if (it.referenceID != 0) {
//                    ids.add(it.referenceID.toString())
//                }
//            }
        }
        return ids
    }

    private suspend fun getCatalogDetail(ids: List<String>): CouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }

}