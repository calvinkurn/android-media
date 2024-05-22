package com.tokopedia.navigation_common.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import androidx.core.animation.addListener
import com.tokopedia.unifyprinciples.UnifyMotion

class BottomNavBarItemRippleTouchListener(
    rootView: View,
    private val rippleView: View
) : View.OnTouchListener {

    private val longPressHandler = Handler(Looper.getMainLooper())
    private val onLongPress = Runnable {
        rootView.performLongClick()
    }
    private val rippleAnimator = ValueAnimator.ofFloat()

    private var currentRippleScale = 0f

    private val durationEnter = UnifyMotion.T3
    private val durationExit = UnifyMotion.T2

    private val interpolatorEnter = UnifyMotion.EASE_OVERSHOOT
    private val interpolatorExit = UnifyMotion.EASE_IN_OUT

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                longPressHandler.removeCallbacks(onLongPress)
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        rippleAnimator.addListener(
                            onEnd = {
                                // if user up before enter  animation finished,
                                // do exit animation after enter animation finished
                                if (currentRippleScale == SCALE_MAX_IMAGE) {
                                    scalingRipple(
                                        end = SCALE_MIN_IMAGE,
                                        duration = durationExit,
                                        pathInterpolator = interpolatorExit,
                                        rippleView = rippleView,
                                        rippleAnimator = rippleAnimator
                                    )
                                }
                            }
                        )
                        // if user up/cancel after enter animation finished (ex: long press)
                        if (currentRippleScale == SCALE_MAX_IMAGE) {
                            // do exit animation
                            scalingRipple(
                                SCALE_MAX_IMAGE,
                                SCALE_MIN_IMAGE,
                                durationExit,
                                interpolatorExit,
                                rippleView,
                                rippleAnimator
                            )
                        }
                    },
                    0L
                )
            }

            MotionEvent.ACTION_DOWN -> {
                longPressHandler.postDelayed(
                    onLongPress,
                    ViewConfiguration.getLongPressTimeout().toLong()
                )
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        // start enter animation ripple
                        scalingRipple(
                            SCALE_MIN_IMAGE,
                            SCALE_MAX_IMAGE,
                            durationEnter,
                            interpolatorEnter,
                            rippleView,
                            rippleAnimator
                        )
                    },
                    0L
                )
            }
        }
        return false
    }

    private fun scalingRipple(
        start: Float = currentRippleScale,
        end: Float,
        duration: Long,
        pathInterpolator: Interpolator,
        rippleView: View,
        rippleAnimator: ValueAnimator
    ) {
        rippleAnimator.setFloatValues(start, end)
        rippleAnimator.removeAllListeners()
        rippleAnimator.removeAllUpdateListeners()
        rippleAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            if (start < end) {
                rippleView.scaleX = value
                rippleView.scaleY = value
            }
            val alpha =
                ((value - SCALE_MIN_IMAGE) / (SCALE_MAX_IMAGE - SCALE_MIN_IMAGE)) * MAX_ALPHA_RIPPLE
            rippleView.alpha = alpha
            currentRippleScale = value
        }
        rippleAnimator.duration = duration
        rippleAnimator.interpolator = pathInterpolator
        rippleAnimator.start()
    }

    companion object {
        private const val SCALE_MAX_IMAGE = 1f
        private const val SCALE_MIN_IMAGE = 0.6f

        private const val MAX_ALPHA_RIPPLE = 0.6f
    }
}
