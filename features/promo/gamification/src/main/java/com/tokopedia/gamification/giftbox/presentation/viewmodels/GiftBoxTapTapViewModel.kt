package com.tokopedia.gamification.giftbox.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapCrackUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapHomeUseCase
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withTimeout
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
    var campaignId: Long = 0
    var waitingForCrackResult = false
    private val CRACK_GIFT_TIME_OUT = 3000L
    private val GET_COUPON_DETAIL_TIME_OUT = 4000L
    var canShowLoader = true

    val giftHomeLiveData: MutableLiveData<LiveDataResult<TapTapBaseEntity>> = MutableLiveData()
    val giftCrackLiveData: MutableLiveData<LiveDataResult<ResponseCrackResultEntity>> = MutableLiveData()
    val couponLiveData: MutableLiveData<LiveDataResult<CouponDetailResponse>> = MutableLiveData()

    fun getGiftBoxHome() {
        if (canShowLoader)
            giftHomeLiveData.postValue(LiveDataResult.loading())
        launchCatchError(block = {
            val response = homeUseCase.getResponse(HashMap())
            giftHomeLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            giftHomeLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun crackGiftBox() {

        waitingForCrackResult = true
        launchCatchError(block = {
            var responseReceived = false
            withTimeout(CRACK_GIFT_TIME_OUT) {
                val response = crackUseCase.getResponse(crackUseCase.getQueryParams(tokenId, campaignId))
                responseReceived = true
                giftCrackLiveData.postValue(LiveDataResult.success(response))
            }
            if (!responseReceived) {
                giftCrackLiveData.postValue(LiveDataResult.error(RuntimeException("Timeout exception")))
            }
        }, onError = {
            giftCrackLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getCouponDetails(benfitItems: List<CrackBenefitEntity>) {
        launchCatchError(block = {
            var responseReceived = false
            withTimeout(GET_COUPON_DETAIL_TIME_OUT) {
                val ids = benfitItems.filter { it.benefitType == BenefitType.COUPON && !it.referenceID.isNullOrEmpty() }
                        .map { it.referenceID }
                val data = getCatalogDetail(ids)
                responseReceived = true
                couponLiveData.postValue(LiveDataResult.success(data))
            }
            if (!responseReceived) {
                couponLiveData.postValue(LiveDataResult.error(RuntimeException("Timeout exception")))
            }

        }, onError = {
            couponLiveData.postValue(LiveDataResult.error(it))
        })
    }

    suspend fun composeApi(entity: ResponseCrackResultEntity): CouponDetailResponse? {
        val ids = mapperGratificationResponseToCouponIds(entity)
        if (ids.isEmpty()) {
            return null
        }
        return getCatalogDetail(ids)
    }

    private fun mapperGratificationResponseToCouponIds(response: ResponseCrackResultEntity): List<String> {
        var ids = arrayListOf<String>()
        response.crackResultEntity.benefits?.forEach {
            if (!it.referenceID.isNullOrEmpty()) {
                ids.add(it.referenceID.toString())
            }
        }
        return ids
    }

    private suspend fun getCatalogDetail(ids: List<String>): CouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }

}