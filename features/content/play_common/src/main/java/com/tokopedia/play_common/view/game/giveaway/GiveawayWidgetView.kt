package com.tokopedia.play_common.view.game.giveaway

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
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play_common.databinding.ViewGiveawayWidgetBinding
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
        binding.flTap.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

            mListener?.onTapTapClicked(this)

            animateVibrate()
        }
    }

    private fun animateVibrate(){
        animator.addListener(animationListener)
        animator.duration = 300L
        animator.start()
    }

    private val clickScaleXAnimation = ObjectAnimator.ofFloat(
        binding.ivTap, View.SCALE_X, 1f, 0.0f
    )
    private val clickScaleYAnimation = ObjectAnimator.ofFloat(
        binding.ivTap, View.SCALE_Y, 1f, 0.0f
    )
    private val shadowAnimation = ObjectAnimator.ofFloat(
        binding.flTap, "elevation", 0f, 1f
    )

    private val scaleBgANim = ObjectAnimator.ofFloat(
        binding.flTap, View.SCALE_X, 1f, 0f
    )

    private val scaleYBgANim = ObjectAnimator.ofFloat(
        binding.flTap, View.SCALE_Y, 1f, 0f
    )

    private val colorAnim = ObjectAnimator.ofArgb(
        binding.flTap, "BackgroundColor", Color.RED, Color.MAGENTA
    )

    private val animator = AnimatorSet().apply {
        interpolator = AccelerateDecelerateInterpolator()
        playTogether(clickScaleXAnimation, clickScaleYAnimation, shadowAnimation, scaleBgANim, scaleYBgANim, colorAnim)
    }

    private val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            binding.ivTap.scaleX = 1f
            binding.flTap.scaleX = 1f
            binding.ivTap.scaleY = 1f
            binding.flTap.scaleY = 1f
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }

    }

    interface Listener {

        fun onTapTapClicked(view: GiveawayWidgetView)
    }
}