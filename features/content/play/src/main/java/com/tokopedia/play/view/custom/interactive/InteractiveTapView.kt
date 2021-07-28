package com.tokopedia.play.view.custom.interactive

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.R
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

/**
 * Created by jegul on 05/07/21
 */
class InteractiveTapView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val flInteractiveTap: FrameLayout
    private val timerTap: TimerUnifySingle
    private val iconTap: IconUnify
    private val tvTapAction: TextView
    private val clTapBackground: RoundedConstraintLayout
    private val lottieConfettiTap: LottieAnimationView
//    private val lottieButtonTap: LottieAnimationView

    private val lottieConfettiListener: Animator.AnimatorListener

    private var mListener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.view_interactive_tap, this)

        flInteractiveTap = view.findViewById(R.id.fl_interactive_tap)
        timerTap = view.findViewById(R.id.timer_tap)
        iconTap = view.findViewById(R.id.icon_tap)
        tvTapAction = view.findViewById(R.id.tv_tap_action)
        clTapBackground = view.findViewById(R.id.cl_tap_background)
        lottieConfettiTap = view.findViewById(R.id.lottie_confetti_tap)
//        lottieButtonTap = view.findViewById(R.id.lottie_button_tap)

        lottieConfettiListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                lottieConfettiTap.progress = 0f
            }

            override fun onAnimationCancel(animation: Animator) {
                lottieConfettiTap.progress = 0f
            }

            override fun onAnimationRepeat(animation: Animator) {}
        }

        setupView(view)
    }

    private fun setupView(view: View) {
//        lottieButtonTap.setAnimationFromUrl(context.getString(R.string.lottie_button_tap), LOTTIE_BUTTON_CACHE_KEY)
//        lottieButtonTap.repeatCount = LottieDrawable.INFINITE
//        lottieButtonTap.playAnimation()

        lottieConfettiTap.addAnimatorListener(lottieConfettiListener)
        lottieConfettiTap.setMinProgress(0.2f)
        lottieConfettiTap.setAnimationFromUrl(context.getString(R.string.lottie_confetti_tap), LOTTIE_CONFETTI_CACHE_KEY)
    }

    fun setTimer(durationInMs: Long, onFinished: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, durationInMs.toInt())

        timerTap.pause()
        timerTap.targetDate = calendar
        timerTap.onFinish = onFinished
        timerTap.resume()
    }

    fun cancelTimer() {
        timerTap.pause()
        timerTap.timer?.cancel()
    }

    fun showFollowMode(shouldShow: Boolean) {
        changeMode(
                if (!shouldShow) Mode.Tap
                else Mode.Follow
        )

        flInteractiveTap.setOnClickListener {
            if (!shouldShow) {
                playTapAnimation()
                mListener?.onTapClicked(this)
            }
            else mListener?.onFollowClicked(this)
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lottieConfettiTap.cancelAnimation()
    }

    private fun changeMode(mode: Mode) {
        when (mode) {
            Mode.Follow -> {
                iconTap.setImage(IconUnify.USER_ADD)
                tvTapAction.text = context.getString(R.string.play_interactive_tap_action_follow_text)
            }
            Mode.Tap -> {
                iconTap.setImage(IconUnify.GIFT)
                tvTapAction.text = context.getString(R.string.play_interactive_tap_action_tap_text)
            }
        }
    }

    private fun playTapAnimation() {
        if (!lottieConfettiTap.isAnimating) lottieConfettiTap.playAnimation()
    }

    companion object {

        private const val LOTTIE_CONFETTI_CACHE_KEY = "CONFETTI_INTERACTIVE_TAP"
        private const val LOTTIE_BUTTON_CACHE_KEY = "BUTTON_INTERACTIVE_TAP"
    }

    private enum class Mode {
        Follow,
        Tap
    }

    interface Listener {

        fun onTapClicked(view: InteractiveTapView)
        fun onFollowClicked(view: InteractiveTapView)
    }
}