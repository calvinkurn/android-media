package com.tokopedia.tokopoints.view.coupondetail

import android.os.Bundle

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@TokoPointScope
class CouponDetailViewModel @Inject constructor(bundle: Bundle, private val repository: CouponDetailRepository) : BaseViewModel(Dispatchers.Main) {

    val detailLiveData = MutableLiveData<Resources<CouponValueEntity>>()
    val swipeDetail = MutableLiveData<CouponSwipeDetail>()
    val pinPageData = MutableLiveData<PinPageData>()
    val onCouponSwipe = MutableLiveData<Resources<CouponSwipeUpdate>>()
    val onReFetch = MutableLiveData<Resources<String>>()
    val onRedeemCoupon = MutableLiveData<Resources<String>>()
    val finish = MutableLiveData<Unit>()
    val userInfo = MutableLiveData<PhoneVerificationResponse>()

    lateinit var data: CouponValueEntity
    lateinit var couponCode: String

    init {
        val code = bundle.getString(CommonConstant.EXTRA_COUPON_CODE)
        val checkCode: Boolean = code?.isEmpty() ?: true
        if (checkCode) {
            detailLiveData.postValue(Loading())
            finish.postValue(Unit)
        } else {
            couponCode = bundle.getString(CommonConstant.EXTRA_COUPON_CODE) as String
            getCouponDetail()
        }
    }

    private fun getCouponDetail() {
        launchCatchError(block = {
            detailLiveData.value = Loading()
            val response = repository.getCouponDetail(couponCode).getSuccessData<CouponDetailOuter>().detail
            detailLiveData.value = Success(response)
            data = response
            checkAndShowSwipe(data)
        }) {
            detailLiveData.value = ErrorMessage(it.message ?: "")
        }
    }

    private fun checkAndShowSwipe(data: CouponValueEntity): Boolean {
        data.swipe.let { swipe ->
            if (swipe.isNeedSwipe) {
                swipeDetail.value = swipe
                return true
            }
        }
        return false
    }


    fun onSwipeComplete() {
        data.swipe.apply {
            if (isNeedSwipe) {
                if (pin.isPinRequire) {
                    pinPageData.value = PinPageData(data.realCode, pin.text)
                } else {
                    swipeMyCoupon(data) //Empty for online partner
                }
            }
        }
    }

    private fun swipeMyCoupon(data: CouponValueEntity) {
        launchCatchError(block = {
            val swipeCouponResponse = repository.swipeMyCoupon(data.realCode, "")
                .getSuccessData<CouponSwipeUpdateOuter>().swipeCoupon
            if (swipeCouponResponse.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                onCouponSwipe.value = Success(swipeCouponResponse)
            } else {
                if (swipeCouponResponse.resultStatus.messages?.isNotEmpty() == true) {
                    onCouponSwipe.value = swipeCouponResponse.resultStatus.messages?.get(0)?.let {
                        ErrorMessage(it)
                    }
                }
            }
        }) {
        }
    }

    fun onErrorButtonClick() {
        getCouponDetail()
    }

    fun reFetchRealCode() {
        launchCatchError(block = {
            val data = repository.reFetchRealCode(couponCode).getSuccessData<CouponDetailOuter>().detail
            onReFetch.value = Success(data.realCode)
        }) {
            onReFetch.value = ErrorMessage(it.message ?: "")
        }
    }

    fun redeemCoupon(code: String, cta: String) {
        launchCatchError(block = {
            repository.redeemCoupon(code).getSuccessData<ApplyCouponBaseEntity>()
            onRedeemCoupon.value = Success(cta)
        }) {
            onRedeemCoupon.value = ErrorMessage(cta)
        }
    }

    fun isPhonerVerfied() {
        launchCatchError(block = {
            val data = repository.getUserPhoneVerificationInfo().getSuccessData<PhoneVerificationResponse>()
            userInfo.value = data
        }) {
        }
    }
}

data class PinPageData(val code: String, val pinText: String)
