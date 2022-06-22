package com.tokopedia.play.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.R
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.play.databinding.ViewGiveawayWidgetBinding
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.setupGiveaway
import java.util.*


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

    private var mListener: Listener? = null

    init {
        setupView()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.timerRemaining.pause()
        mListener = null
    }

    override fun setClickable(clickable: Boolean) {
        super.setClickable(clickable)

        binding.flTap.isClickable = clickable
    }

    fun getHeader(): GameHeaderView {
        return binding.headerView
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setTitle(title: String) {
        getHeader().setupGiveaway(title)
    }

    fun showTimer(shouldShow: Boolean) {
        binding.timerRemaining.showWithCondition(shouldShow)
    }

    fun setTargetTime(targetTime: Calendar, onFinished: () -> Unit) {
        binding.timerRemaining.apply {
            pause()

            targetDate = targetTime
            onFinish = onFinished

            resume()
        }
    }

    private fun setupView() {
        setTitle("")
        getHeader().setHint(
            context.getString(R.string.play_giveaway_header_hint)
        )

        binding.flTap.isHapticFeedbackEnabled = true
        binding.flTap.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

            animateTap()

            mListener?.onTapTapClicked(this)
        }

        fun setupLottieBg() {
            binding.tapLottieBg.setAnimationFromUrl(context.getString(R.string.lottie_bg_tap))
            binding.tapLottieBg.repeatCount = LottieDrawable.INFINITE
            binding.tapLottieBg.playAnimation()
        }

        fun setupLottieButton() {
            binding.ivTap.setAnimationFromUrl(context.getString(R.string.lottie_button_tap))
            binding.ivTap.repeatCount = LottieDrawable.INFINITE
            binding.ivTap.playAnimation()
        }

        setupLottieButton()
        setupLottieBg()
    }

    private fun animateTap() {
        tapAnimator.addListener(animationListener)
        tapAnimator.duration = 200L
        tapAnimator.start()
    }


    private val clickScaleXAnimation = ObjectAnimator.ofFloat(
        binding.flTap, View.SCALE_X, 1f, 0.6f
    )
    private val clickScaleYAnimation = ObjectAnimator.ofFloat(
        binding.flTap, View.SCALE_Y, 1f, 0.6f
    )

    private val tapAnimator = AnimatorSet().apply {
        playTogether(clickScaleXAnimation, clickScaleYAnimation)
    }

    private val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            binding.flTap.scaleX = 1f
            binding.flTap.scaleY = 1f
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationRepeat(animation: Animator?) {}

    }

    interface Listener {

        fun onTapTapClicked(view: GiveawayWidgetView)
    }
}