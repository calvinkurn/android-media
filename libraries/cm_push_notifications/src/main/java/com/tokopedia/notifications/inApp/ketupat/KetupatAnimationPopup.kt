package com.tokopedia.notifications.inApp.ketupat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.RenderMode
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifications.R
import com.tokopedia.notifications.databinding.LayoutInappAnimationBinding
import com.tokopedia.notifications.domain.data.Benefits
import com.tokopedia.notifications.domain.data.GamiScratchCardCrack
import kotlinx.android.synthetic.main.layout_inapp_animation.view.*


open class KetupatAnimationPopup(context: Context, attrs: AttributeSet?, val activity: Activity):
    ConstraintLayout(context, attrs) {

    private val binding =
       LayoutInappAnimationBinding.inflate(LayoutInflater.from(context), this, true)
    private var eventSlicingStart: MotionEvent? = null
    private var eventSlicingEnd: MotionEvent? = null
    private var numberOfCoupons = 0
    private var numberOfRetry = 0

     open fun loadLottieAnimation() {
         handleLottieSlice()
         binding.lottieViewPopup.setRenderMode(RenderMode.HARDWARE)
         binding.lottieViewPopup.setMinFrame("Tutorial")
         binding.lottieViewPopup.setMaxFrame(119)
         onCloseClick(binding.root)
         onParentContainerClick(binding.root)
         getCouponData()
    }

    private fun getCouponData() {
        AnimationPopupGqlGetData().getAnimationCrackCouponData({
            showPopupAnimation()
            hideCouponErrorState()
            handleCouponData(it)
        },{
            handleCouponError()
            hidePopupAnimationAndCoupons()
        })
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
            handleRetry()
            hideCouponErrorState()
        }
    }

    private fun handleRetry() {
        if (numberOfRetry < 1) {
            numberOfRetry += 1
            getCouponData()
        } else {
            binding.btnErrorState.gone()
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

    private fun showPopupAnimation() {
        binding.lottieViewPopup.visible()
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

    private fun showCoupons(numberOfCoupons: Int?) {
        with(binding) {
            ivButtonShare.visible()
            when (numberOfCoupons) {
                1 -> {
                    ivCoupon1.visible()
                }

                2 -> {
                    ivCoupon1.visible()
                    ivCoupon2.visible()
                }

                3 -> {
                    ivCoupon1.visible()
                    ivCoupon2.visible()
                    ivCoupon3.visible()
                }

                4 -> {
                    ivCoupon1.visible()
                    ivCoupon2.visible()
                    ivCoupon3.visible()
                    ivCoupon4.visible()
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

    private fun handleLottieSlice() {
        binding.lottieViewPopup.setOnTouchListener(touchListener)
    }

    @SuppressLint("ClickableViewAccessibility")
    private var touchListener = OnTouchListener { _, event ->
        when (event?.action) {
            // A gesture is starting, move the path to the pointer's location
            MotionEvent.ACTION_DOWN -> {
                eventSlicingStart = MotionEvent.obtain(event)
            }

            MotionEvent.ACTION_UP -> {
                eventSlicingEnd = MotionEvent.obtain(event)
                getDirection()
            }
            else -> return@OnTouchListener false
        }
        return@OnTouchListener true
    }

    private fun getDirection() {
        eventSlicingStart?.let {
            eventSlicingEnd?.let { it1 ->
                val direction = MyGestureListener().getDirection(it.x, it.y, it1.x, it1.y)
                val mdisp = activity.windowManager.defaultDisplay
                val mdispSize = Point()
                mdisp.getSize(mdispSize)
                val isSliced = MyGestureListener().getSlicePercent(it.x, it.y, it1.x, it1.y, mdispSize.x, mdispSize.y, direction)
                if (isSliced) {
                    playAnimationInDirection(direction)
                }
            }
        }
    }

    private fun playAnimationInDirection(direction: MyGestureListener.Direction) {
        when(direction) {
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
        showCoupons(numberOfCoupons)
        playPrizeSound(activity.applicationContext)
        binding.lottieViewPopup.loop(false)
        couponAnimation()
        couponButtonAnimation()
    }

    private fun playPrizeSound(context: Context?) {
        var rewardSoundManager: AudioManager? = null
        context?.let { soundIt ->
            if (rewardSoundManager == null) {
                rewardSoundManager = AudioFactory.createAudio(soundIt)
            }
            rewardSoundManager?.playAudio(R.raw.gf_giftbox_prize)
        }
    }

    private fun onCloseClick(view: View) {
        val onClickListener = OnClickListener { _: View? ->
            (view.parent as ViewGroup).removeView(view)
        }
        binding.icClose.setOnClickListener(onClickListener)
    }
    private fun onParentContainerClick(view: View) {
        val onClickListener = OnClickListener { _: View? ->
            (view.parent as ViewGroup).removeView(view)
        }
        binding.parentContainer.setOnClickListener(onClickListener)
    }

    private fun couponAnimation() {
        binding.couponContainer.visible()
        val layout = binding.couponContainer
        val anim: Animation = AnimationUtils.loadAnimation(activity, R.anim.coupon_scale)
        layout.startAnimation(anim)
    }

    private fun couponButtonAnimation() {
        binding.ivButtonShare.visible()
        val layout = binding.ivButtonShare
        val anim: Animation = AnimationUtils.loadAnimation(activity, R.anim.button_translate)
        layout.startAnimation(anim)
    }

}
