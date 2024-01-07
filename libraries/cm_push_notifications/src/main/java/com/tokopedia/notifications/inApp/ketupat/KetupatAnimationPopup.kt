package com.tokopedia.notifications.inApp.ketupat

import android.annotation.SuppressLint
import android.app.Activity
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.airbnb.lottie.RenderMode
import com.tokopedia.notifications.databinding.LayoutInappAnimationBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


open class KetupatAnimationPopup(context: Context, attrs: AttributeSet?, val activity: Activity):
    ConstraintLayout(context, attrs) {

    private val binding =
       LayoutInappAnimationBinding.inflate(LayoutInflater.from(context), this, true)
    private var eventSlicingStart: MotionEvent? = null
    private var eventSlicingEnd: MotionEvent? = null

     open fun loadLottieAnimation() {
        handleLottieSlice()
         binding.lottieViewPopup.setRenderMode(RenderMode.HARDWARE)
        binding.lottieViewPopup.setMinFrame("Tutorial")
        binding.lottieViewPopup.setMaxFrame(119)
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
                Log.d("coordinates->>>>", "x1 = ${it.x}, y1 = ${it.y}, x2 = ${it1.x}, y2 = ${it1.y}")
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun playAnimation() {
        binding.lottieViewPopup.playAnimation()
        GlobalScope.launch {
            delay(3000)
            binding.lottieViewPopup.setMinProgress(0.72f)
            binding.lottieViewPopup.setMaxProgress(1.0f)
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
//        playPrizeSound(context)
        binding.lottieViewPopup.loop(false)
    }

    private fun playPrizeSound(context: Context?) {
        var rewardSoundManager: AudioManager? = null
        context?.let { soundIt ->
            if (rewardSoundManager == null) {
                rewardSoundManager = AudioFactory.createAudio(soundIt)
            }
//            rewardSoundManager?.playAudio(android.R.raw.)
        }
    }

}
