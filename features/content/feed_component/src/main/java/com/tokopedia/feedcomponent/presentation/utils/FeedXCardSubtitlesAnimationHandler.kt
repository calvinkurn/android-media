package com.tokopedia.feedcomponent.presentation.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Handler
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
        override fun onViewAttachedToWindow(v: View?) {
            startTimer()
        }

        override fun onViewDetachedFromWindow(v: View?) {
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
        val START_DELAY = 2000L
        val INTERVAL = 3000L

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
                val translateAnimPropOne =
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -dpToPx(48))
                val translateAnimObjOne: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v1, translateAnimPropOne)

                val translateAnimPropTwo =
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dpToPx(48), 0f)
                val translateAnimObjTwo: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v2, translateAnimPropTwo)

                animatorSet?.cancel()
                animatorSet = AnimatorSet()
                animatorSet?.playTogether(
                    translateAnimObjOne,
                    translateAnimObjTwo
                )
                animatorSet?.duration = 600L
                animatorSet?.start()

                Handler().postDelayed({ afterAnimationComplete() }, animatorSet?.duration ?: 600L)
            }
        }
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}
