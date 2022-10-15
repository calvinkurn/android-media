package com.tokopedia.play.broadcaster.view.custom

import android.animation.*
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.play.broadcaster.R
import kotlin.math.ceil


class PlayTimerCountDown @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val progressCircular: LottieAnimationView
    private val info: LottieAnimationView
    private val countText: TextView

    private lateinit var timer: CountDownTimer

    /**
     * Animation area
     */
    private lateinit var rotateAnimator: Animator

    private val textAnimatorIn: AnimatorSet
    private val textAnimatorOut: AnimatorSet
    private val animatorProgressCircularOut: AnimatorSet
    private val animatorInfoOut: AnimatorSet

    private val textAnimatorSet = AnimatorSet()

    init {
        val view = View.inflate(context, R.layout.widget_play_timer_count_down, this)

        progressCircular = view.findViewById(R.id.progress_circular)

        countText = view.findViewById(R.id.count_text)
        info = view.findViewById(R.id.info)

        countText.alpha = 0f

        textAnimatorIn = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_scale_alpha) as AnimatorSet
        textAnimatorOut = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_reverse_alpha) as AnimatorSet
        textAnimatorSet.playSequentially(textAnimatorIn, textAnimatorOut)
        animatorProgressCircularOut = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_reverse_scale_alpha) as AnimatorSet
        animatorInfoOut = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_translate_alpha) as AnimatorSet
        textAnimatorIn.setTarget(countText)
        textAnimatorOut.setTarget(countText)
        animatorProgressCircularOut.setTarget(progressCircular)
        animatorInfoOut.setTarget(info)

        progressCircular.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                progressCircular.removeAllAnimatorListeners()
                rotateAnimator.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationStart(animation: Animator) {
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (::timer.isInitialized) timer.cancel()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
            textAnimatorOut.end()
            textAnimatorIn.end()
            textAnimatorSet.end()
            animatorProgressCircularOut.end()
            animatorInfoOut.end()
        }
    }

    fun startCountDown(property: AnimationProperty, listener: Listener? = null){
        val textInterval = property.textCountDownInterval

        setupTextCountAnimator(textInterval)
        setupRotationAnimator(property.fullRotationInterval)

        timer = object : CountDownTimer(textInterval * property.totalCount, textInterval) {

            private var alreadyTick = false
            private val intervalDouble = textInterval.toDouble()

            override fun onFinish() {
                animatorInfoOut.start()
                animatorProgressCircularOut.start()
                listener?.onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                if (!alreadyTick) {
                    progressCircular.playAnimation()
                    info.playAnimation()
                    alreadyTick = true
                }

                val secondsLeft = ceil(millisUntilFinished / intervalDouble).toInt()
                countText.text = secondsLeft.toString()

                textAnimatorSet.start()

                listener?.onTick(millisUntilFinished)
            }
        }.start()
    }

    private fun setupTextCountAnimator(interval: Long) {
        val multiplier = interval / MILLIS_IN_SECOND
        textAnimatorIn.duration = 225 * multiplier
        textAnimatorOut.duration = 150 * multiplier
        textAnimatorOut.startDelay = 400 * multiplier
    }

    private fun setupRotationAnimator(interval: Long) {
        rotateAnimator = getRotateAnimator(interval)
    }

    private fun getRotateAnimator(interval: Long): Animator {
        val rotateAnimator = ObjectAnimator.ofFloat(progressCircular, "rotation", 0f, 360f)
        rotateAnimator.interpolator = LinearInterpolator()
        rotateAnimator.duration = interval
        rotateAnimator.repeatCount = ValueAnimator.INFINITE
        return rotateAnimator
    }

    class AnimationProperty private constructor(
        val fullRotationInterval: Long,
        val textCountDownInterval: Long,
        val totalCount: Int
    ) {

        class Builder {

            private var fullRotationInterval: Long = MILLIS_IN_SECOND
            private var textCountDownInterval: Long = MILLIS_IN_SECOND
            private var totalCount: Int = 3

            fun setFullRotationInterval(intervalInMillis: Long): Builder {
                fullRotationInterval = intervalInMillis
                return this
            }

            fun setTextCountDownInterval(intervalInMillis: Long): Builder {
                textCountDownInterval = intervalInMillis
                return this
            }

            fun setTotalCount(totalCount: Int): Builder {
                this.totalCount = totalCount
                return this
            }

            fun build(): AnimationProperty {
                return AnimationProperty(
                    fullRotationInterval = fullRotationInterval,
                    textCountDownInterval = textCountDownInterval,
                    totalCount = totalCount
                )
            }
        }
    }

    companion object {

        private const val MILLIS_IN_SECOND = 1000L
    }

    interface Listener {

        fun onTick(milisUntilFinished: Long)

        fun onFinish()
    }
}
