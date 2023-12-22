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
    companion object {
        private const val CRACK_GIFT_TIME_OUT = 3000L
        private const val GET_COUPON_DETAIL_TIME_OUT = 4000L
    }

    @Volatile
    var tokenId: String = ""

    @Volatile
    var campaignId: Long = 0

    var waitingForCrackResult = false
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
            withTimeout(CRACK_GIFT_TIME_OUT) {
                val response = crackUseCase.getResponse(crackUseCase.getQueryParams(tokenId, campaignId))
                giftCrackLiveData.postValue(LiveDataResult.success(response))
            }
        }, onError = {
            giftCrackLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getCouponDetails(benefitItems: List<CrackBenefitEntity>) {
        launchCatchError(block = {
            withTimeout(GET_COUPON_DETAIL_TIME_OUT) {
                val data = couponDetailUseCase.getResponse(mapBenefitsToIds(benefitItems))
                couponLiveData.postValue(LiveDataResult.success(data))
            }
        }, onError = {
            couponLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun mapBenefitsToIds(benefitItems: List<CrackBenefitEntity>): List<String> {
        return benefitItems.filter { it.benefitType == BenefitType.COUPON && it.referenceID.isNotEmpty() }.map { it.referenceID }
    }
}
