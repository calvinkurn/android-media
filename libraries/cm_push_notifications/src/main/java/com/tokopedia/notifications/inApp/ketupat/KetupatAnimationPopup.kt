package com.tokopedia.notifications.inApp.ketupat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.RenderMode
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.databinding.LayoutInappAnimationBinding
import com.tokopedia.notifications.domain.data.PopUpContent
import kotlinx.android.synthetic.main.layout_inapp_animation.view.*

open class KetupatAnimationPopup(context: Context, attrs: AttributeSet?, val activity: Activity) :
    ConstraintLayout(context, attrs) {

    private val layoutInflater = LayoutInflater.from(context)
    private val binding =
        LayoutInappAnimationBinding.inflate(layoutInflater, this, true)
    private var eventSlicingStart: MotionEvent? = null
    private var myGestureListener = MyGestureListener()
    private var eventSlicingEnd: MotionEvent? = null
    private val animationPopupGtmTracker = AnimationPopupGtmTracker()
    private var crackCouponHandler = CrackCouponHandler(binding, layoutInflater, animationPopupGtmTracker, activity)
    private var slug: String? = ""
    private var scratchCardId: String = ""
    private var ketupatSlashCallBack: KetupatSlashCallBack? = null

    open fun loadLottieAnimation(
        slug: String?,
        popUpContent: PopUpContent,
        scratchCardId: String,
        ketupatSlashCallBack: KetupatSlashCallBack? = null
    ) {
        try {
            this.slug = slug
            this.scratchCardId = scratchCardId
            this.ketupatSlashCallBack = ketupatSlashCallBack
            loadAnimationFromURl(popUpContent)
            handleLottieSlice()
            binding.lottieViewPopup.setRenderMode(RenderMode.HARDWARE)
            onCloseClick(binding.root)
            onParentContainerClick(binding.root)
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

    private fun loadAnimationFromURl(popUpContent: PopUpContent) {
        popUpContent.assets?.get(0)?.let {
            binding.lottieViewPopup.setAnimationFromUrl(it.value)
            binding.lottieViewPopup.setMinFrame("Tutorial")
            binding.lottieViewPopup.setMaxFrame(59)
            Handler().postDelayed({
                binding.icClose.visible()
            }, 600)
            animationPopupGtmTracker.sendPopupImpressionEvent(scratchCardId)
        }
        popUpContent.assets?.get(1)?.let {
            crackCouponHandler.url = it.value.toString()
            activity.applicationContext?.let { context ->
                crackCouponHandler.rewardSoundManager = createAudioManager(context)
            }
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
                val direction = myGestureListener.getDirection(it.x, it.y, it1.x, it1.y)
                val mdisp = activity.windowManager.defaultDisplay
                val mdispSize = Point()
                mdisp.getSize(mdispSize)
                val isSliced = myGestureListener.getSlicePercent(it.x, it.y, it1.x, it1.y, mdispSize.x, mdispSize.y, direction)
                if (isSliced && !crackCouponHandler.isCouponCracked()) {
                    crackCouponHandler.getCouponData(slug, direction)
                    binding.loaderCoupon.visible()
                    binding.lottieViewPopup.pauseAnimation()
                    ketupatSlashCallBack?.ketuPatSlashed()
                }
            }
            animationPopupGtmTracker.sendPopupInteractionEvent(scratchCardId)
        }
    }

    private fun onCloseClick(view: View) {
        val onClickListener = OnClickListener { _: View? ->
            (view.parent as ViewGroup).removeView(view)
            crackCouponHandler.destroySound()
            animationPopupGtmTracker.sendPopupCloseEvent(scratchCardId)
        }
        binding.icClose.setOnClickListener(onClickListener)
    }

    private fun onParentContainerClick(view: View) {
        val (percentageDx, percentageDy) = myGestureListener.getGesturePercent()
        val onClickListener = OnClickListener { _: View? ->
            if (percentageDx < 6 || percentageDy < 6) {
                (view.parent as ViewGroup).removeView(view)
                crackCouponHandler.destroySound()
                animationPopupGtmTracker.sendPopupCloseEvent(scratchCardId)
            }
        }
        binding.parentContainer.setOnClickListener(onClickListener)
    }

    private fun createAudioManager(context: Context?): AudioManager? {
        var rewardSoundManager: AudioManager? = null
        context?.let {
            if (rewardSoundManager == null) {
                rewardSoundManager = AudioFactory.createAudio(it)
            }
        }
        return rewardSoundManager
    }
}
