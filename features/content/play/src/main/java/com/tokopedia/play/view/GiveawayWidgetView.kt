package com.tokopedia.play.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ViewGiveawayWidgetBinding
import com.tokopedia.play.view.custom.interactive.InteractiveTapView
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.setupGiveaway
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class GiveawayWidgetView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewGiveawayWidgetBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private val lottieConfettiListener: Animator.AnimatorListener

    private var mListener: Listener? = null

    private val isButtonLottieLoaded = AtomicBoolean(false)
    private val isConfettiLottieLoaded = AtomicBoolean(false)

    private val buttonLottieRetryCount = AtomicInteger(0)
    private val confettiLottieRetryCount = AtomicInteger(0)

    init {
        setupView()

        lottieConfettiListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                binding.lottieConfettiTap.progress = 0f
            }

            override fun onAnimationCancel(animation: Animator) {
                binding.lottieConfettiTap.progress = 0f
            }

            override fun onAnimationRepeat(animation: Animator) {}
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.timerRemaining.pause()
        mListener = null
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setTitle(title: String) {
        binding.headerView.setupGiveaway(title)
    }

    fun setTargetTime(targetTime: Calendar, onFinished: () -> Unit) {
        binding.timerRemaining.apply {
            pause()

            targetDate = targetTime
            onFinish = onFinished

            resume()
        }
    }

    fun getHeader(): GameHeaderView {
        return binding.headerView
    }

    private fun setupView() {
        setTitle("")
        binding.lottieButtonTap.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

            mListener?.onTapTapClicked(this)

            animateView()
        }
    }

    private fun animateView(){
        fun setupLottieButton() {
            binding.lottieButtonTap.setFailureListener {
                if (it is UnknownHostException && buttonLottieRetryCount.getAndIncrement() < 3) loadButtonTapLottie()
            }
            binding.lottieButtonTap.addLottieOnCompositionLoadedListener {
                isButtonLottieLoaded.compareAndSet(false, true)
                invalidateLottieState()
            }
            binding.lottieButtonTap.repeatCount = LottieDrawable.INFINITE
            binding.lottieButtonTap.playAnimation()
            loadButtonTapLottie()
        }

        fun setupLottieConfetti() {
            binding.lottieConfettiTap.setFailureListener {
                if (it is UnknownHostException && confettiLottieRetryCount.getAndIncrement() < 3) loadConfettiTapLottie()
            }
            binding.lottieConfettiTap.addLottieOnCompositionLoadedListener {
                isConfettiLottieLoaded.compareAndSet(false, true)
                invalidateLottieState()
            }
            binding.lottieConfettiTap.addAnimatorListener(lottieConfettiListener)
            binding.lottieConfettiTap.setMinProgress(0.3f)
            loadConfettiTapLottie()
        }

        setupLottieButton()
        setupLottieConfetti()
    }

    private fun loadButtonTapLottie() {
        binding.lottieButtonTap.setAnimationFromUrl(context.getString(R.string.lottie_button_tap),
            LOTTIE_BUTTON_CACHE_KEY
        )
    }

    private fun loadConfettiTapLottie() {
        binding.lottieConfettiTap.setAnimationFromUrl(context.getString(R.string.lottie_confetti_tap),
            LOTTIE_CONFETTI_CACHE_KEY
        )
    }

    private fun invalidateLottieState() = synchronized(this) {
        if (isButtonLottieLoaded.get() && isConfettiLottieLoaded.get()) {
            binding.lottieButtonTap.visibility = View.VISIBLE
            binding.lottieConfettiTap.visibility = View.VISIBLE
        } else {
            binding.lottieButtonTap.visibility = View.GONE
            binding.lottieConfettiTap.visibility = View.GONE
        }
    }


    companion object {
       private const val LOTTIE_CONFETTI_CACHE_KEY = "CONFETTI_INTERACTIVE_TAP"
       private const val LOTTIE_BUTTON_CACHE_KEY = "BUTTON_INTERACTIVE_TAP"
   }

    interface Listener {

        fun onTapTapClicked(view: GiveawayWidgetView)
    }
}