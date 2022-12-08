package com.tokopedia.feedcomponent.presentation.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.View
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created By : Muhammad Furqan on 11/11/22
 */
class FeedXCardSubtitlesAnimationHandler(
    private val firstContainer: WeakReference<com.tokopedia.unifyprinciples.Typography>,
    private val secondContainer: WeakReference<com.tokopedia.unifyprinciples.Typography>
) {

    var subtitles: List<String> = emptyList()
    private var currentPositionAnimationInfo = 0

    private var visibleContainer = firstContainer
    private var invisibleContainer = secondContainer
    private var isAnimationStarted = false
    private var animatorSet: AnimatorSet? = null
    private var timer: Timer? = null

    private val onAttachListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            startTimer()
        }

        override fun onViewDetachedFromWindow(v: View) {
            stopAnimation()
        }
    }

    fun checkToCancelTimer() {
        firstContainer.get()?.addOnAttachStateChangeListener(onAttachListener)
    }

    private fun animateView() {
        isAnimationStarted = true

        setDataIntoViews()
        animateTwoViews(visibleContainer, invisibleContainer)
    }

    private fun setDataIntoViews() {
        val visibleDataPos =
            if (currentPositionAnimationInfo == subtitles.size) 0 else currentPositionAnimationInfo
        val invisibleDataPos =
            if (currentPositionAnimationInfo + 1 >= subtitles.size) 0 else currentPositionAnimationInfo + 1
        currentPositionAnimationInfo = visibleDataPos

        visibleContainer.get()?.text = subtitles[visibleDataPos]
        invisibleContainer.get()?.text = subtitles[invisibleDataPos]

    }

    fun stopAnimation() {
        timer?.cancel()
        reset()
    }

    fun startTimer() {
        if (timer == null) {
            setDataIntoViews()
        }
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                visibleContainer.get()?.post {
                    animateView()
                }
            }
        }, START_DELAY, INTERVAL)
    }

    private fun afterAnimationComplete() {
        //switch container reference
        val tempContainer = visibleContainer
        visibleContainer = invisibleContainer
        invisibleContainer = tempContainer

        //increment pos
        currentPositionAnimationInfo += 1

        //reset position
        if (currentPositionAnimationInfo == subtitles.size)
            currentPositionAnimationInfo = 0
    }

    private fun reset() {
        firstContainer.get()?.clearAnimation()
        secondContainer.get()?.clearAnimation()

        firstContainer.get()?.alpha = 1f
        firstContainer.get()?.translationY = 0f
        secondContainer.get()?.alpha = 0f
        secondContainer.get()?.translationY = dpToPx(48)
        currentPositionAnimationInfo = 0

        visibleContainer = firstContainer
        invisibleContainer = secondContainer
    }

    @SuppressLint("Recycle")
    private fun animateTwoViews(
        viewOne: WeakReference<com.tokopedia.unifyprinciples.Typography>,
        viewTwo: WeakReference<com.tokopedia.unifyprinciples.Typography>
    ) {

        viewOne.get()?.let { v1 ->
            viewTwo.get()?.let { v2 ->
                val alphaAnimPropOne = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
                val alphaAnimObjOne: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v1, alphaAnimPropOne)

                val translateAnimPropOne =
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -dpToPx(20))
                val translateAnimObjOne: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v1, translateAnimPropOne)

                val alphaAnimPropTwo = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                val alphaAnimObjTwo: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v2, alphaAnimPropTwo)

                val translateAnimPropTwo =
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dpToPx(20), 0f)
                val translateAnimObjTwo: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v2, translateAnimPropTwo)

                translateAnimObjTwo.addListener(object : AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        setDataIntoViews()
                        afterAnimationComplete()
                        translateAnimObjTwo.removeAllListeners()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }

                })

                animatorSet?.cancel()
                animatorSet = AnimatorSet()
                animatorSet?.playTogether(
                    alphaAnimObjOne,
                    translateAnimObjOne,
                    alphaAnimObjTwo,
                    translateAnimObjTwo
                )
                animatorSet?.duration = DURATION
                animatorSet?.start()
            }
        }
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    companion object {
        private const val START_DELAY = 2000L
        private const val INTERVAL = 2000L
        private const val DURATION = 600L
    }
}
