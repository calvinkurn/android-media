package com.tokopedia.play.broadcaster.view.custom

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.play.broadcaster.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlin.coroutines.CoroutineContext


class PlayTimerCountDown @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CoroutineScope {

    private val progressCircular: LottieAnimationView
    private val info: LottieAnimationView
    private val countText: TextView
    private var count: Int = 3

    @ObsoleteCoroutinesApi
    private val ticker = ticker(2000,0)

    /**
     * Animation area
     */
    private var rotateAnimation: RotateAnimation

    private val textAnimatorIn: AnimatorSet
    private val textAnimatorOut: AnimatorSet
    private val animatorProgressCircularOut: AnimatorSet
    private val animatorInfoOut: AnimatorSet


    private val mainJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mainJob + Dispatchers.Main

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.widget_play_timer_count_down, null, false)
        addView(view)

        progressCircular = view.findViewById(R.id.progress_circular)

        countText = view.findViewById(R.id.count_text)
        info = view.findViewById(R.id.info)

        countText.text = count.toString()
        countText.alpha = 0f
        val degree = 360f * (count * 2 / 3)
        Log.d("TEST", "degree :${degree}")
        rotateAnimation = RotateAnimation(
                0f,
                degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = (1000 * (count * 2)).toLong()
        rotateAnimation.interpolator = LinearInterpolator()

        textAnimatorIn = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_scale_alpha) as AnimatorSet
        textAnimatorOut = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_reverse_alpha) as AnimatorSet
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
                progressCircular.startAnimation(rotateAnimation)
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {
                initTicker()
            }
        })

        progressCircular.playAnimation()
        info.playAnimation()
    }

    private fun initTicker(){
        launch {
            delay(50)
            repeat(count + 1){
                ticker.receive()
                if(count != 0) {
                    countText.text = count.toString()
                    textAnimatorIn.start()
                    delay(1600)
                    textAnimatorOut.start()
                    if(count == 1){
                        animatorInfoOut.start()
                        animatorProgressCircularOut.start()
                    }
                    delay(300)
                }
                count--
            }
        }
    }

    fun setCountDown(number: Int){
        count = number
        rotateAnimation = RotateAnimation(0f, 360f * (count * 2 / 3), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = (1000 * (count * 2)).toLong()
        rotateAnimation.interpolator = LinearInterpolator()
    }

    fun start(){
        progressCircular.playAnimation()
        info.playAnimation()
    }
}