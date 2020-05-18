package com.tokopedia.thankyou_native.presentation.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.thankyou_native.R


class ThankYouLoadingView
    : FrameLayout {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    ) {
        initView()
    }

    private lateinit var flyingView: ConstraintLayout

    private lateinit var constraintSet: ConstraintSet

    private lateinit var character: ImageView
    private lateinit var mainBackground: ImageView
    private lateinit var eyes: ImageView
    private lateinit var cloud: ImageView
    private lateinit var coinOne: ImageView
    private lateinit var coinTwo: ImageView
    private lateinit var coinThree: ImageView
    private lateinit var coinFour: ImageView
    private lateinit var coinFive: ImageView
    private lateinit var coinSix: ImageView

    private var isAnimating: Boolean = false


    private var rotationCount = 1
    private var cloudRotationAlter = false


    private val dp_24_pixel = convertDpToPixel(24F)
    private val dp_150_pixel = convertDpToPixel(150F)
    private val dp_4_pixel = convertDpToPixel(4F)
    private val dp_10_pixel = convertDpToPixel(10F)
    private val dp_16_pixel = convertDpToPixel(16F)
    private val dp_7_pixel = convertDpToPixel(7F)
    private val dp_13_pixel = convertDpToPixel(13F)
    private val dp_20_pixel = convertDpToPixel(20F)

    init {
        initView()
    }

    private fun initView() {
        val flyingView = LayoutInflater.from(context)
                .inflate(R.layout.thank_layout_flying, null, false)
        this.addView(flyingView)
        if (flyingView is ConstraintLayout)
            this.flyingView = flyingView
        character = flyingView.findViewById(R.id.character)
        mainBackground = flyingView.findViewById(R.id.mainBackground)
        eyes = flyingView.findViewById(R.id.eyes)
        cloud = flyingView.findViewById(R.id.cloud)
        coinOne = flyingView.findViewById(R.id.coinOne)
        coinTwo = flyingView.findViewById(R.id.coinTwo)
        coinThree = flyingView.findViewById(R.id.coinThree)
        coinFour = flyingView.findViewById(R.id.coinFour)
        coinFive = flyingView.findViewById(R.id.coinFive)
        coinSix = flyingView.findViewById(R.id.coinSix)
        setImageResources()
    }

    private fun setImageResources() {
        loadImageResource(mainBackground, R.drawable.thanks_img_bg_clouds)
        loadImageResource(character, R.drawable.thanks_img_characters)
        loadImageResource(cloud, R.drawable.thanks_img_cloud)
        loadImageResource(eyes, R.drawable.thanks_img_open_eyes)
        loadImageResource(coinOne, R.drawable.thanks_flat_coin)
        loadImageResource(coinTwo, R.drawable.thanks_flat_coin)
        loadImageResource(coinThree, R.drawable.thanks_flip_coin)
        loadImageResource(coinFour, R.drawable.thanks_flip_coin)
        loadImageResource(coinFive, R.drawable.thanks_flat_coin)
        loadImageResource(coinSix, R.drawable.thanks_flat_coin)
    }

    private fun loadImageResource(imageView: ImageView, @DrawableRes drawableResId: Int) {
        Glide.with(context)
                .load(drawableResId)
                .into(imageView)
    }

    fun startAnimation() {
        if (isAnimating)
            return
        if (visibility == View.VISIBLE) {
            if (!::constraintSet.isInitialized) {
                constraintSet = ConstraintSet()
                constraintSet.load(context, R.layout.thank_layout_flying)
            }
            constraintSet.applyTo(flyingView)
            isAnimating = true
            rotationCount = 1
            cloudRotationAlter = false
            startAnimatingFlyingView()
        }
    }

    fun stopAnimation() {
        if (!isAnimating)
            return
        isAnimating = false
        character.clearAnimation()
        cloud.clearAnimation()
        eyes.clearAnimation()
        coinOne.clearAnimation()
        coinTwo.clearAnimation()
        coinThree.clearAnimation()
        coinFour.clearAnimation()
        coinFive.clearAnimation()
        coinSix.clearAnimation()
    }

    private fun startAnimatingFlyingView() {
        levitate(character, dp_24_pixel, true)
        levitateEyes(eyes, dp_24_pixel, true)
        animateCloud(cloud, -dp_150_pixel, true)
        translateCoin(coinOne, -dp_24_pixel, -dp_4_pixel, -25F, true)
        translateCoin(coinTwo, dp_10_pixel, -dp_7_pixel, 45F, true)
        translateCoin(coinThree, -dp_13_pixel, dp_20_pixel, 25F, true)
        translateCoin(coinFour, -dp_4_pixel, dp_16_pixel, 25F, true)
        translateCoin(coinFive, dp_16_pixel, dp_10_pixel, 25F, true)
        translateCoin(coinSix, dp_10_pixel, -dp_7_pixel, 25F, true)
    }

    private fun animateCloud(movableView: View, x: Float, animated: Boolean) {
        if (animated && parentAnimationLock()) {
            val yourDuration: Long = if (cloudRotationAlter) {
                cloudRotationAlter = false
                0L
            } else {
                cloudRotationAlter = true
                6000L
            }
            val yourInterpolator: TimeInterpolator = DecelerateInterpolator()
            movableView.animate().translationXBy(x).setDuration(yourDuration)
                    .setInterpolator(yourInterpolator).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            animateCloud(movableView, -x, true)
                        }
                    })
        }
    }

    fun levitate(movableView: View, Y: Float, animated: Boolean) {
        if (animated && parentAnimationLock()) {
            val yourDuration: Long = 750
            val yourInterpolator: TimeInterpolator = DecelerateInterpolator()
            movableView.animate().translationYBy(Y).setDuration(yourDuration)
                    .setInterpolator(yourInterpolator).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            levitate(movableView, -Y, true)
                        }
                    })
        }
    }

    fun levitateEyes(movableView: ImageView, Y: Float, animated: Boolean) {
        if (animated && parentAnimationLock()) {
            if (rotationCount == 3 || rotationCount == 7) {
                movableView.setImageResource(R.drawable.thanks_img_close_eyes)
            } else {
                movableView.setImageResource(R.drawable.thanks_img_open_eyes)
            }
            rotationCount++
            if (rotationCount > 7) {
                rotationCount = 4
            }
            val yourDuration: Long = 750
            val yourInterpolator: TimeInterpolator = DecelerateInterpolator()
            movableView.animate().translationYBy(Y).setDuration(yourDuration)
                    .setInterpolator(yourInterpolator).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            levitateEyes(movableView, -Y, true)
                        }
                    })
        }
    }

    fun translateCoin(movableView: View, x: Float, y: Float, rotate: Float, animated: Boolean) {
        if (animated && parentAnimationLock()) {
            val yourDuration: Long = 750L
            val yourInterpolator: TimeInterpolator = DecelerateInterpolator()
            movableView.animate().translationXBy(x).translationYBy(y).rotation(rotate)
                    .setDuration(yourDuration)
                    .setInterpolator(yourInterpolator).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            translateCoin(movableView, -x, -y, -rotate, true)
                        }
                    })
        }
    }

    private fun parentAnimationLock(): Boolean {
        return isAnimating && !isPowerSaveMode() && (visibility == View.VISIBLE)
    }


    private fun isPowerSaveMode(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val powerManager =
                    context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return powerManager.isPowerSaveMode
        }
        return false
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    private fun convertDpToPixel(dp: Float): Float {
        return dp * (context.resources
                .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


}
