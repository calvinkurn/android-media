package com.tokopedia.play.broadcaster.view.custom

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.play.broadcaster.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.math.ceil


class PlayTimerCountDown @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CoroutineScope {

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

    private val mainJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mainJob + Dispatchers.Main

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
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                progressCircular.removeAllAnimatorListeners()
                rotateAnimator.start()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (::timer.isInitialized) timer.cancel()
    }

    fun startCountDown(seconds: Int, interval: Long, listener: Listener? = null){
        setupAnimator(seconds, interval)

        timer = object : CountDownTimer(seconds * MILLIS_IN_SECOND, MILLIS_IN_SECOND) {

            private var alreadyTick = false
            private val millisDouble = MILLIS_IN_SECOND.toDouble()

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

                val secondsLeft = ceil(millisUntilFinished / millisDouble).toInt()
                countText.text = secondsLeft.toString()

                textAnimatorSet.start()

                listener?.onTick(millisUntilFinished)
            }
        }.start()
    }

    private fun setupAnimator(seconds: Int, interval: Long) {
        val multiplier = interval / MILLIS_IN_SECOND
        rotateAnimator = getRotateAnimator(seconds, interval)
        textAnimatorIn.duration = 225 * multiplier
        textAnimatorOut.duration = 150 * multiplier
        textAnimatorOut.startDelay = 400 * multiplier
    }

    private fun getRotateAnimator(seconds: Int, interval: Long): Animator {
        val rotateAnimator = ObjectAnimator.ofFloat(progressCircular, "rotation", 0f, 360f)
        rotateAnimator.interpolator = LinearInterpolator()
        rotateAnimator.duration = interval
        rotateAnimator.repeatCount = seconds
        return rotateAnimator
    }

    companion object {

        private const val MILLIS_IN_SECOND = 1000L
    }

    interface Listener {

        fun onTick(milisUntilFinished: Long)

        fun onFinish()
    }
}