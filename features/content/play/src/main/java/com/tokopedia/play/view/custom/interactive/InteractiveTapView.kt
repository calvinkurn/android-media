package com.tokopedia.play.view.custom.interactive

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.R
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by jegul on 05/07/21
 */
class InteractiveTapView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val flInteractiveTap: FrameLayout
    private val flInteractiveFollow: FrameLayout
    private val timerTap: TimerUnifySingle
    private val tvTapAction: TextView
    private val clTapBackground: RoundedConstraintLayout
    private val lottieConfettiTap: LottieAnimationView
    private val lottieButtonTap: LottieAnimationView
    private val groupLottieTap: Group

    private val lottieConfettiListener: Animator.AnimatorListener

    private var mListener: Listener? = null

    private val isButtonLottieLoaded = AtomicBoolean(false)
    private val isConfettiLottieLoaded = AtomicBoolean(false)

    private val buttonLottieRetryCount = AtomicInteger(0)
    private val confettiLottieRetryCount = AtomicInteger(0)

    init {
        val view = View.inflate(context, R.layout.view_interactive_tap, this)

        flInteractiveTap = view.findViewById(R.id.fl_interactive_tap)
        flInteractiveFollow = view.findViewById(R.id.fl_interactive_follow)
        timerTap = view.findViewById(R.id.timer_tap)
        tvTapAction = view.findViewById(R.id.tv_tap_action)
        clTapBackground = view.findViewById(R.id.cl_tap_background)
        lottieConfettiTap = view.findViewById(R.id.lottie_confetti_tap)
        lottieButtonTap = view.findViewById(R.id.lottie_button_tap)
        groupLottieTap = view.findViewById(R.id.group_lottie_tap)

        lottieConfettiListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                lottieConfettiTap.progress = LOTTIE_START_PROGRESS
            }

            override fun onAnimationCancel(animation: Animator) {
                lottieConfettiTap.progress = LOTTIE_START_PROGRESS
            }

            override fun onAnimationRepeat(animation: Animator) {}
        }

        flInteractiveTap.setOnClickListener {
            mListener?.onTapClicked(this)
        }

        lottieButtonTap.setOnClickListener {
            mListener?.onTapClicked(this)
        }

        flInteractiveFollow.setOnClickListener {
            mListener?.onFollowClicked(this)
        }

        setupView(view)
    }

    private fun setupView(view: View) {
        fun setupLottieButton() {
            lottieButtonTap.setFailureListener {
                if (it is UnknownHostException && buttonLottieRetryCount.getAndIncrement() < MAX_RETRY_COUNT) loadButtonTapLottie()
            }
            lottieButtonTap.addLottieOnCompositionLoadedListener {
                isButtonLottieLoaded.compareAndSet(false, true)
                invalidateLottieState()
            }
            lottieButtonTap.repeatCount = LottieDrawable.INFINITE
            lottieButtonTap.playAnimation()
            loadButtonTapLottie()
        }

        fun setupLottieConfetti() {
            lottieConfettiTap.setFailureListener {
                if (it is UnknownHostException && confettiLottieRetryCount.getAndIncrement() < MAX_RETRY_COUNT) loadConfettiTapLottie()
            }
            lottieConfettiTap.addLottieOnCompositionLoadedListener {
                isConfettiLottieLoaded.compareAndSet(false, true)
                invalidateLottieState()
            }
            lottieConfettiTap.addAnimatorListener(lottieConfettiListener)
            lottieConfettiTap.setMinProgress(LOTTIE_CONFETTI_MIN_PROGRESS)
            loadConfettiTapLottie()
        }

        setupLottieButton()
        setupLottieConfetti()
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
        if (shouldShow) {
            groupLottieTap.invisible() //Invisible to keep the guideline position
            flInteractiveTap.gone()
            flInteractiveFollow.visible()

            tvTapAction.text = context.getString(R.string.play_interactive_tap_action_follow_text)
        } else {
            groupLottieTap.visible()
            invalidateLottieState()
            flInteractiveFollow.gone()

            tvTapAction.text = context.getString(R.string.play_interactive_tap_action_tap_text)
        }

        lottieButtonTap.setOnClickListener(getButtonTapClickedListener(isFollowMode = shouldShow))
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun playTapAnimation() {
        if (!lottieConfettiTap.isAnimating) lottieConfettiTap.playAnimation()
    }

    private fun getButtonTapClickedListener(isFollowMode: Boolean) = OnClickListener {
        if (!isFollowMode) {
            playTapAnimation()
            mListener?.onTapClicked(this)
        }
        else mListener?.onFollowClicked(this)
    }

    private fun loadButtonTapLottie() {
        lottieButtonTap.setAnimationFromUrl(context.getString(R.string.lottie_button_tap), LOTTIE_BUTTON_CACHE_KEY)
    }

    private fun loadConfettiTapLottie() {
        lottieConfettiTap.setAnimationFromUrl(context.getString(R.string.lottie_confetti_tap), LOTTIE_CONFETTI_CACHE_KEY)
    }

    private fun invalidateLottieState() = synchronized(this) {
        if (isButtonLottieLoaded.get() && isConfettiLottieLoaded.get()) {
            mListener?.onAnimationLoadedFromUrl(this@InteractiveTapView)

            lottieButtonTap.visibility = View.VISIBLE
            lottieConfettiTap.visibility = View.VISIBLE
            flInteractiveTap.visibility = View.GONE
        } else {
            flInteractiveTap.visibility = View.VISIBLE
            lottieButtonTap.visibility = View.GONE
            lottieConfettiTap.visibility = View.GONE
        }
    }

    companion object {

        private const val LOTTIE_CONFETTI_CACHE_KEY = "CONFETTI_INTERACTIVE_TAP"
        private const val LOTTIE_BUTTON_CACHE_KEY = "BUTTON_INTERACTIVE_TAP"

        private const val MAX_RETRY_COUNT = 3

        private const val LOTTIE_CONFETTI_MIN_PROGRESS = 0.4f
        private const val LOTTIE_START_PROGRESS = 0f
    }

    interface Listener {

        fun onTapClicked(view: InteractiveTapView)
        fun onFollowClicked(view: InteractiveTapView)

        fun onAnimationLoadedFromUrl(view: InteractiveTapView)
    }
}