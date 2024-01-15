package com.tokopedia.notifications.inApp.ketupat

import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifications.databinding.LayoutInappAnimationBinding
import com.tokopedia.notifications.domain.data.Benefits
import com.tokopedia.notifications.domain.data.GamiScratchCardCrack
import com.tokopedia.notifications.R
import com.tokopedia.unifycomponents.ImageUnify


class CrackCouponHandler(val binding: LayoutInappAnimationBinding, val layoutInflater: LayoutInflater) {

    private var numberOfCoupons = 0
    private var numberOfRetry = 0

     fun getCouponData(slug: String?) {
        AnimationPopupGqlGetData().getAnimationCrackCouponData({
            showPopupAnimation()
            hideCouponErrorState()
            handleCouponData(it)
        },{
            handleCouponError(slug)
            hidePopupAnimationAndCoupons()
        }, slug)
    }

    private fun showPopupAnimation() {
        binding.lottieViewPopup.visible()
    }

    private fun handleCouponData(gamiScratchCardCrack: GamiScratchCardCrack) {
        gamiScratchCardCrack.benefits?.size?.let {
            numberOfCoupons = it
        }
        createCoupons(numberOfCoupons, gamiScratchCardCrack)
//        loadCoupons(numberOfCoupons, gamiScratchCardCrack)
    }

    private fun handleCouponError(slug: String?) {
        showCouponErrorState()
        binding.btnErrorState.setOnClickListener {
            if (numberOfRetry < 0) {
                numberOfRetry += 1
                getCouponData(slug)
                hideCouponErrorState()
                binding.loaderAnimation.visible()
            } else {
                binding.btnErrorState.gone()
            }
        }
    }

    private fun showCouponErrorState() {
        with(binding) {
            loaderAnimation.gone()
            animationErrorState.visible()
            btnErrorState.visible()
        }
        setCloseButtonMargin(362)
    }

    private fun hidePopupAnimationAndCoupons() {
        binding.lottieViewPopup.invisible()
        hideCoupons()
    }

    private fun hideCouponErrorState() {
        with(binding) {
            animationErrorState.gone()
            btnErrorState.gone()

        }
    }

    private fun createCoupons(numberOfCoupons: Int, gamiScratchCardCrack: GamiScratchCardCrack) {
        gamiScratchCardCrack.benefits?.let {
            for (i in 0 until numberOfCoupons) {
                val coupon = layoutInflater.inflate(R.layout.layout_coupon_animation, binding.couponContainer, false)
                val couponImage = (coupon as View).findViewById<ImageUnify>(R.id.iv_coupon)
                couponImage.loadImage(getCouponURl(i, it))
                binding.couponContainer.addView(coupon)
            }
        }
    }

    private fun getCouponURl(couponNumber: Int, couponList: List<Benefits>): String {
        couponList[couponNumber].assets?.get(1)?.value?.let { url ->
            return url
        }
        return ""
    }

    private fun hideCoupons() {
        with(binding) {
            ivButtonShare.gone()
        }
    }

     private fun setCloseButtonMargin(bottomMargin: Int) {
        binding.icClose.setMargin(0,0, 154, bottomMargin)
    }
}
