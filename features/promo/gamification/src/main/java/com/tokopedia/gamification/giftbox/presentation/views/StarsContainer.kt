package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout

class StarsContainer : FrameLayout {

    lateinit var imageStar1: AppCompatImageView
    lateinit var imageStar2: AppCompatImageView
    lateinit var imageStar3: AppCompatImageView
    lateinit var imageStar4: AppCompatImageView

    lateinit var images: Array<AppCompatImageView>
    var startY = 0f
    var startX = 0f
    var isTablet = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(com.tokopedia.gamification.R.layout.view_stars_container, this, true)

        imageStar1 = findViewById(R.id.image_star1)
        imageStar2 = findViewById(R.id.image_star2)
        imageStar3 = findViewById(R.id.image_star3)
        imageStar4 = findViewById(R.id.image_star4)

        images = arrayOf(imageStar1, imageStar2, imageStar3, imageStar4)
        setupAlphaStars()

        var deviceIsTablet = context?.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet)
        deviceIsTablet?.let {
            isTablet = it
        }

        doOnLayout {
            setStartPositionOfStars(width / 2f, height / 2f)
        }
    }

    fun getStarsAnimationList(giftBoxTop: Int): List<Animator> {

        val firstAnimationDuration = 1000L
        val finalAlpha = 1f
        var finalScale = 1f


        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, finalAlpha)
        val scaleYAnim = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, finalScale)
        val scaleXAnim = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, finalScale)

        fun startEndPoints(view: View, starsCoord: StarsCoord): ObjectAnimator {
            val propX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, startX, starsCoord.endX)
            val propY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, startY, starsCoord.endY)
            val animator = ObjectAnimator.ofPropertyValuesHolder(view, propX, propY)
            return animator
        }

        fun rotationAnimator(view: View, startAngle: Float, endAngle: Float): ObjectAnimator {
            val prop = PropertyValuesHolder.ofFloat(View.ROTATION, startAngle, endAngle)
            val rotation = ObjectAnimator.ofPropertyValuesHolder(view, prop)
            rotation.repeatMode = ValueAnimator.REVERSE
            rotation.repeatCount = ValueAnimator.INFINITE
            rotation.duration = 1200L
            return rotation
        }

        val firstTranslationList = arrayListOf<Animator>()
        val lastTransitionList = arrayListOf<Animator>()
        val scaleAnimatorList = arrayListOf<Animator>()
        val alphaAnimatorList = arrayListOf<Animator>()
        val rotationAnimatorList = arrayListOf<Animator>()


        val startEndPoints = arrayListOf<StarsCoord>()
        var w = width
        var h = height
        var xOffset = 0

        var tlStarEndY = 0f
        if (isTablet) {
            w = dpToPx(500f).toInt()
            xOffset = (width - w) / 2
            tlStarEndY = giftBoxTop - dpToPx(250f)
        } else {
            tlStarEndY = h * 0.05f
        }

        var tlStarCoord = StarsCoord(xOffset - (images[0].width / 2f), tlStarEndY)
        var trStarCoord = StarsCoord(xOffset + w - images[1].width.toFloat() + dpToPx(20f), h * 0.18f)
        var blStarCoord = StarsCoord(xOffset + (w * 0.04f), h * 0.54f)
        var brStarCoord = StarsCoord(xOffset + (w - w * 0.20f), h * 0.56f)

        startEndPoints.add(tlStarCoord)
        startEndPoints.add(trStarCoord)
        startEndPoints.add(blStarCoord)
        startEndPoints.add(brStarCoord)

        //first translation and scale
        images.forEachIndexed { index, view ->
            val interpolator = CubicBezierInterpolator(0.22, 1.0, 0.36, 1.0)

            val translationAnimation = startEndPoints(view, startEndPoints[index])
            translationAnimation.duration = firstAnimationDuration
            translationAnimation.interpolator = interpolator
            firstTranslationList.add(translationAnimation)

            val scaleAnimation = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnim, scaleXAnim)
            scaleAnimation.duration = firstAnimationDuration
            scaleAnimation.interpolator = interpolator
            scaleAnimatorList.add(scaleAnimation)

            val alphaAnimation = ObjectAnimator.ofPropertyValuesHolder(view, alphaProp)
            alphaAnimation.duration = firstAnimationDuration
            alphaAnimation.interpolator = interpolator
            alphaAnimatorList.add(alphaAnimation)
        }

        //rotation

        val tlRotaion = rotationAnimator(images[0], 0f, 16f)
        val tRRotaion = rotationAnimator(images[1], 0f, 16f)
        val blRotaion = rotationAnimator(images[2], 0f, -16f)
        val brRotaion = rotationAnimator(images[3], 0f, 0f)

        rotationAnimatorList.add(tlRotaion)
        rotationAnimatorList.add(tRRotaion)
        rotationAnimatorList.add(blRotaion)
        rotationAnimatorList.add(brRotaion)

        rotationAnimatorList.forEachIndexed { index, it ->
            it.startDelay = index * 200L
        }

        //after scale translation

        fun lastTranslation(view: View, endX: Float, endY: Float, startCoord: StarsCoord): ObjectAnimator {
            val propX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, startCoord.endX, startCoord.endX + endX)
            val propY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, startCoord.endY, startCoord.endY + endY)

            val rotation = ObjectAnimator.ofPropertyValuesHolder(view, propX, propY)
            rotation.repeatMode = ValueAnimator.REVERSE
            rotation.repeatCount = ValueAnimator.INFINITE
            rotation.duration = 1200L
            return rotation
        }

        val tlLastTranslation =
                lastTranslation(images[0], dpToPx(5f), dpToPx(10f), tlStarCoord)
        val tRLastTranslation = lastTranslation(images[1], dpToPx(2f), dpToPx(10f), trStarCoord)
        val blLastTranslation = lastTranslation(images[2], dpToPx(2f), dpToPx(5f), blStarCoord)
        val bRLastTranslation = lastTranslation(images[3], 0f, dpToPx(10f), brStarCoord)

        lastTransitionList.add(tlLastTranslation)
        lastTransitionList.add(tRLastTranslation)
        lastTransitionList.add(blLastTranslation)
        lastTransitionList.add(bRLastTranslation)

        lastTransitionList.forEachIndexed { index, it ->
            it.startDelay = index * 200L
        }

        val animatorsList = arrayListOf<AnimatorSet>()
        for (index in 0..3) {
            val tlAnimatorSet = AnimatorSet()
            tlAnimatorSet.playTogether(firstTranslationList[index], scaleAnimatorList[index], alphaAnimatorList[index])
            tlAnimatorSet.playSequentially(firstTranslationList[index], rotationAnimatorList[index])
            tlAnimatorSet.playTogether(rotationAnimatorList[index], lastTransitionList[index])
            tlAnimatorSet.startDelay = 400L
            animatorsList.add(tlAnimatorSet)
        }
        return animatorsList
    }

    fun setStartPositionOfStars(cX: Float, cY: Float) {
        images.forEach {
            it.translationX = cX
            it.translationY = cY
        }
        startX = cX
        startY = cY
    }

    fun setupAlphaStars() {
        images.forEach {
            it.alpha = 0f
            it.scaleX = 2f
            it.scaleY = 2f
        }
    }

    fun dpToPx(dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

data class StarsCoord(val endX: Float, val endY: Float)
