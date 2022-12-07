package com.tokopedia.play_common.view.game

import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ViewGiveawayWidgetBinding
import com.tokopedia.play_common.util.AnimationUtils.addSpringAnim
import java.util.*


/**
 * Created by kenny.hadisaputra on 12/04/22
 */
@Suppress("MagicNumber")
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

    private val scaleX = addSpringAnim(
        view = binding.ivTap, property = SpringAnimation.SCALE_X, startPosition = 0f,
        finalPosition = 1f, stiffness = SpringForce.STIFFNESS_MEDIUM, dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY, velocity = 0f)

    private val scaleY = addSpringAnim(
        view = binding.ivTap, property = SpringAnimation.SCALE_Y, startPosition = 0f,
        finalPosition = 1f, stiffness = SpringForce.STIFFNESS_MEDIUM, dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY, velocity = 0f)

    init {
        setupView()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.timerRemaining.pause()
        mListener = null
        cancelAllAnim()
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
        /**
         * Need to reset every time we tap
         */
        binding.ivTap.apply {
            scaleX = 0f
            scaleY = 0f
        }
        scaleX.start()
        scaleY.start()
    }

    private fun cancelAllAnim() {
        scaleY.cancel()
        scaleX.cancel()
    }

    interface Listener {

        fun onTapTapClicked(view: GiveawayWidgetView)
    }
}