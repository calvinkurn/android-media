package com.tokopedia.notifications.inApp.ketupat

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.databinding.LayoutInappAnimationBinding
import com.tokopedia.notifications.domain.data.Benefits
import com.tokopedia.notifications.domain.data.GamiScratchCardCrack
import com.tokopedia.unifycomponents.ImageUnify
import android.view.View.OnClickListener
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.promoui.common.CouponImageView

class CrackCouponHandler(
    val binding: LayoutInappAnimationBinding,
    private val layoutInflater: LayoutInflater,
    private val animationPopupGtmTracker: AnimationPopupGtmTracker,
    private val activity: Activity
) {
    private var numberOfCoupons = 0
    private var numberOfRetry = 0
    private var isCouponCracked = false
    var url = ""
    var buttonShareAppLink = ""
    var scratchCardId = ""
    var CatalogId = ""
    var CatalogSlug = ""

    fun getCouponData(slug: String?, direction: MyGestureListener.Direction) {
        try {
            AnimationPopupGqlGetData().getAnimationCrackCouponData({
                getTrackerData(it)
                showPopupAnimation()
                hideCouponErrorState()
                handleCouponData(it, direction)
            }, {
                handleCouponError(slug, direction)
                hidePopupAnimationAndCoupons()
            }, slug)
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P2,
                "KETUPAT_ANIMATION_POPUP",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                    "data" to ""
                )
            )
        }
    }

    private fun getTrackerData(gamiScratchCardCrack: GamiScratchCardCrack) {
        gamiScratchCardCrack.scratchCard?.let {
            scratchCardId = it.id.toString()
        }

        gamiScratchCardCrack.benefits
    }

    private fun showPopupAnimation() {
        binding.lottieViewPopup.visible()
        binding.loaderAnimation.gone()
        binding.lottieViewPopup.resumeAnimation()
        if (numberOfRetry < 3 && numberOfRetry != 0) {
            resetCloseButtonMargin()
        }
    }

    private fun handleCouponData(gamiScratchCardCrack: GamiScratchCardCrack, direction: MyGestureListener.Direction) {
        gamiScratchCardCrack.benefits?.size?.let {
            numberOfCoupons = it
        }
        createCoupons(numberOfCoupons, gamiScratchCardCrack, direction)
        createShareButton(gamiScratchCardCrack)
    }

    private fun handleCouponError(slug: String?, direction: MyGestureListener.Direction) {
        showCouponErrorState()
        binding.btnErrorState.setOnClickListener {
            if (numberOfRetry < 3) {
                numberOfRetry += 1
                getCouponData(slug, direction)
                hideCouponErrorState()
                animationPopupGtmTracker.sendErrorRetryButtonEvent(scratchCardId)
                binding.loaderAnimation.visible()
            } else {
                binding.btnErrorState.gone()
                animationPopupGtmTracker.sendErrorUnRetryableImpressionEvent(scratchCardId)
            }
        }
    }

    private fun showCouponErrorState() {
        with(binding) {
            animationErrorState.visible()
            if (numberOfRetry < 3) {
                btnErrorState.visible()
            }
        }
        setCloseButtonMargin()
        animationPopupGtmTracker.sendErrorImpressionEvent(scratchCardId)
    }

    private fun hidePopupAnimationAndCoupons() {
        binding.lottieViewPopup.invisible()
        binding.loaderCoupon.gone()
        hideCoupons()
    }

    private fun hideCouponErrorState() {
        with(binding) {
            loaderCoupon.gone()
            animationErrorState.gone()
            btnErrorState.gone()
        }
    }

    // creating coupons
    private fun createCoupons(
        numberOfCoupons: Int,
        gamiScratchCardCrack: GamiScratchCardCrack,
        direction: MyGestureListener.Direction
    ) {
        gamiScratchCardCrack.benefits?.let {
            for (i in 0 until numberOfCoupons) {
                val coupon = layoutInflater.inflate(R.layout.layout_coupon_animation, binding.couponContainer, false)
                val couponImage = (coupon as View).findViewById<CouponImageView>(R.id.iv_coupon)
                couponImage.loadImageFitCenter(getCouponURl(i, it))
                binding.couponContainer.addView(coupon)
            }
            playAnimationInDirection(direction)
        }
    }

    private fun playAnimationInDirection(direction: MyGestureListener.Direction) {
        when (direction) {
            MyGestureListener.Direction.up_right -> {
                binding.lottieViewPopup.setMaxFrame(569)
                binding.lottieViewPopup.setMinFrame(420)
            }
            MyGestureListener.Direction.up_left -> {
                binding.lottieViewPopup.setMaxFrame(269)
                binding.lottieViewPopup.setMinFrame(120)
            }
            MyGestureListener.Direction.down_left -> {
                binding.lottieViewPopup.setMaxFrame(269)
                binding.lottieViewPopup.setMinFrame(120)
            }
            MyGestureListener.Direction.down_right -> {
                binding.lottieViewPopup.setMaxFrame(569)
                binding.lottieViewPopup.setMinFrame(420)
            }
            MyGestureListener.Direction.horizontol -> {
                binding.lottieViewPopup.setMaxFrame(419)
                binding.lottieViewPopup.setMinFrame(270)
            }
            MyGestureListener.Direction.vertical -> {
                binding.lottieViewPopup.setMaxFrame(719)
                binding.lottieViewPopup.setMinFrame(570)
            }
            else -> {
                binding.lottieViewPopup.setMaxFrame(269)
                binding.lottieViewPopup.setMinFrame(120)
            }
        }
        playPrizeSound(activity.applicationContext)
        binding.lottieViewPopup.loop(false)
        couponAnimation()
        couponButtonAnimation()
        isCouponCracked = true
    }

    private fun playPrizeSound(context: Context?) {
        var rewardSoundManager: AudioManager? = null
        context?.let {
            if (rewardSoundManager == null) {
                rewardSoundManager = AudioFactory.createAudio(it)
            }
            rewardSoundManager?.playAudio(R.raw.gf_giftbox_prize)
        }
    }

    fun isCouponCracked(): Boolean {
        return isCouponCracked
    }
    private fun createShareButton(gamiScratchCardCrack: GamiScratchCardCrack) {
        gamiScratchCardCrack.cta?.let {
            binding.ivButtonShare.loadImage(it.imageURL)
            buttonShareAppLink = it.appLink.toString()
        }
        onButtonShareClick(binding.root)
    }

    private fun getCouponURl(couponNumber: Int, couponList: List<Benefits>): String {
        couponList[couponNumber].assets?.get(1)?.value?.let { url ->
            return url
        }
        return ""
    }

    private fun navigateToAppLink() {
        if (buttonShareAppLink.isNotEmpty() && buttonShareAppLink != "null") {
            RouteManager.route(activity, buttonShareAppLink)
        }
    }

    private fun hideCoupons() {
        with(binding) {
            ivButtonShare.gone()
        }
    }

    private fun couponAnimation() {
        binding.couponContainer.visible()
        binding.loaderCoupon.gone()
        val layout = binding.couponContainer
        val anim: Animation = AnimationUtils.loadAnimation(activity, R.anim.coupon_scale)
        layout.startAnimation(anim)
//        animationPopupGtmTracker.sendCouponImpressionEvent()
    }

    private fun couponButtonAnimation() {
        binding.ivButtonShare.visible()
        val layout = binding.ivButtonShare
        val anim: Animation = AnimationUtils.loadAnimation(activity, R.anim.button_translate)
        layout.startAnimation(anim)
//        animationPopupGtmTracker.sendCtaButtonImpressionEvent()
    }

    private fun onButtonShareClick(view: View) {
        val onClickListener = OnClickListener { _: View? ->
            (view.parent as ViewGroup).removeView(view)
            navigateToAppLink()
//            animationPopupGtmTracker.sendCtaButtonClickEvent()
        }
        binding.ivButtonShare.setOnClickListener(onClickListener)
    }

    private fun setCloseButtonMargin() {
//        binding.icClose.translationY = bottomMargin
        binding.icClose.translationX = 32.0f
    }

    private fun resetCloseButtonMargin() {
//        binding.icClose.translationY = 180.0f
        binding.icClose.translationX = 0.0f
    }
}
