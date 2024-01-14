package com.tokopedia.notifications.inApp.ketupat

import androidx.core.view.marginBottom
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifications.databinding.LayoutInappAnimationBinding
import com.tokopedia.notifications.domain.data.Benefits
import com.tokopedia.notifications.domain.data.GamiScratchCardCrack

class CrackCouponHandler(val binding: LayoutInappAnimationBinding) {

    private var numberOfCoupons = 0
    private var numberOfRetry = 0

     fun getCouponData() {
        AnimationPopupGqlGetData().getAnimationCrackCouponData({
            showPopupAnimation()
            hideCouponErrorState()
            handleCouponData(it)
        },{
            handleCouponError()
            hidePopupAnimationAndCoupons()
        })
    }

    private fun showPopupAnimation() {
        binding.lottieViewPopup.visible()
    }

    private fun handleCouponData(gamiScratchCardCrack: GamiScratchCardCrack) {
        gamiScratchCardCrack.benefits?.size?.let {
            numberOfCoupons = it
        }
        loadCoupons(numberOfCoupons, gamiScratchCardCrack)
    }

    private fun handleCouponError() {
        showCouponErrorState()
        binding.btnErrorState.setOnClickListener {
            if (numberOfRetry < 0) {
                numberOfRetry += 1
                getCouponData()
                hideCouponErrorState()
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
    }

    private fun hidePopupAnimationAndCoupons() {
        binding.lottieViewPopup.invisible()
        hideCoupons()
    }

    private fun hideCouponErrorState() {
        with(binding) {
            loaderAnimation.visible()
            animationErrorState.gone()
            btnErrorState.gone()

        }
    }

    private fun loadCoupons(numberOfCoupons: Int, gamiScratchCardCrack: GamiScratchCardCrack) {
        with(binding) {
            gamiScratchCardCrack.cta?.let {
                ivButtonShare.loadImage(it.imageURL)
            }
            gamiScratchCardCrack.benefits?.let {
                when (numberOfCoupons) {
                    1 -> {
                        ivCoupon1.loadImage(getCouponURl(0, it))
                    }

                    2 -> {
                        ivCoupon1.loadImage(getCouponURl(0, it))
                        ivCoupon2.loadImage(getCouponURl(1, it))
                    }

                    3 -> {
                        ivCoupon1.loadImage(getCouponURl(0, it))
                        ivCoupon2.loadImage(getCouponURl(1, it))
                        ivCoupon3.loadImage(getCouponURl(2, it))
                    }

                    4 -> {
                        ivCoupon1.loadImage(getCouponURl(0, it))
                        ivCoupon2.loadImage(getCouponURl(1, it))
                        ivCoupon3.loadImage(getCouponURl(2, it))
                        ivCoupon4.loadImage(getCouponURl(3, it))
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun getCouponURl(couponNumber: Int, couponList: List<Benefits>): String {
        couponList[couponNumber].assets?.get(1)?.value?.let { url ->
            return url
        }
        return ""
    }

     fun showCoupons() {
        with(binding) {
            ivButtonShare.visible()
            when (numberOfCoupons) {
                1 -> {
                    ivCoupon1.visible()
                }

                2 -> {
                    ivCoupon1.visible()
                    ivCoupon2.visible()
                    setCloseButtonMargin(372)
                }

                3 -> {
                    ivCoupon1.visible()
                    ivCoupon2.visible()
                    ivCoupon3.visible()
                    setCloseButtonMargin(202)
                }

                4 -> {
                    ivCoupon1.visible()
                    ivCoupon2.visible()
                    ivCoupon3.visible()
                    ivCoupon4.visible()
                    setCloseButtonMargin(102)
                }

                else -> {

                }
            }
        }
    }

    private fun hideCoupons() {
        with(binding) {
            ivButtonShare.gone()
            ivCoupon1.invisible()
            ivCoupon2.gone()
            ivCoupon3.gone()
            ivCoupon4.gone()
        }
    }

     private fun setCloseButtonMargin(bottomMargin: Int) {
        binding.icClose.setMargin(0,0, 144, bottomMargin)
    }
}
