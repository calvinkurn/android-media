package com.tokopedia.play.broadcaster.view.custom

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlin.math.ceil

/**
 * Created By : Jonathan Darwin on October 12, 2021
 */
class PlayTimerLiveCountDown @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val countText: TextView
    private val btnCancel: UnifyButton
    private val loader: LoaderUnify

    private lateinit var timer: CountDownTimer

    /**
     * Animation area
     */
    private val textAnimatorIn: AnimatorSet
    private val textAnimatorOut: AnimatorSet
    private val animatorInfoOut: AnimatorSet

    private val textAnimatorSet = AnimatorSet()

    init {
        val view = View.inflate(context, R.layout.widget_play_timer_live_count_down, this)

        countText = view.findViewById(R.id.count_text)
        btnCancel = view.findViewById(R.id.btn_play_cancel_live_stream)
        loader = view.findViewById(R.id.play_loader_count_down)

        countText.alpha = 0f

        textAnimatorIn = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_scale_alpha) as AnimatorSet
        textAnimatorOut = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_reverse_alpha) as AnimatorSet
        textAnimatorSet.playSequentially(textAnimatorIn, textAnimatorOut)
        animatorInfoOut = AnimatorInflater.loadAnimator(context, R.animator.play_timer_count_down_translate_alpha) as AnimatorSet
        textAnimatorIn.setTarget(countText)
        textAnimatorOut.setTarget(countText)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (::timer.isInitialized) timer.cancel()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
            endAllAnimation()
        }
    }

    private fun endAllAnimation() {
        textAnimatorOut.end()
        textAnimatorIn.end()
        textAnimatorSet.end()
        animatorInfoOut.end()
    }

    fun startCountDown(property: AnimationProperty, listener: Listener? = null){
        loader.gone()
        btnCancel.visible()

        val textInterval = property.textCountDownInterval

        setupTextCountAnimator(textInterval)

        btnCancel.setOnClickListener {
            timer.cancel()
            endAllAnimation()
            gone()
            listener?.onCancelLiveStream()
        }

        timer = object : CountDownTimer(textInterval * property.totalCount, textInterval) {

            private var alreadyTick = false
            private val intervalDouble = textInterval.toDouble()

            override fun onFinish() {
                animatorInfoOut.start()
                loader.visible()
                btnCancel.invisible()
                listener?.onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                if (!alreadyTick) {
                    alreadyTick = true
                }

                val secondsLeft = ceil(millisUntilFinished / intervalDouble).toInt()
                countText.text = secondsLeft.toString()

                textAnimatorSet.start()

                listener?.onTick(millisUntilFinished)
            }
        }.start()
    }

    fun setBottomWindowInsets() {
        btnCancel.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setupTextCountAnimator(interval: Long) {
        val multiplier = interval / MILLIS_IN_SECOND
        textAnimatorIn.duration = 225 * multiplier
        textAnimatorOut.duration = 150 * multiplier
        textAnimatorOut.startDelay = 400 * multiplier
    }

    class AnimationProperty private constructor(
        val textCountDownInterval: Long,
        val totalCount: Int
    ) {

        class Builder {
            private var textCountDownInterval: Long = MILLIS_IN_SECOND
            private var totalCount: Int = 3

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

        fun onCancelLiveStream()
    }
}