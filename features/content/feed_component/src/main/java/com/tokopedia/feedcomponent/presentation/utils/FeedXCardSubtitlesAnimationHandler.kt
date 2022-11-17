package com.tokopedia.feedcomponent.presentation.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import android.view.View
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created By : Muhammad Furqan on 11/11/22
 */
class FeedXCardSubtitlesAnimationHandler(
    val firstContainer: WeakReference<com.tokopedia.unifyprinciples.Typography>,
    val secondContainer: WeakReference<com.tokopedia.unifyprinciples.Typography>
) {

    lateinit var subtitles: List<String>
    private var currentPositionSubtitle = 0

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
        animateTwoViews(visibleContainer, invisibleContainer)
        setDataIntoViews()
    }

    private fun setDataIntoViews() {
        if (!::subtitles.isInitialized) return

        val visibleDataPos =
            if (currentPositionSubtitle == subtitles.size) 0 else currentPositionSubtitle
        val invisibleDataPos =
            if (currentPositionSubtitle + 1 >= subtitles.size) 0 else currentPositionSubtitle + 1

        visibleContainer.get()?.text = subtitles[visibleDataPos]
        invisibleContainer.get()?.text = subtitles[invisibleDataPos]

//        currentPositionSubtitle += 1
//        if (currentPositionSubtitle >= subtitles.size) {
//            currentPositionSubtitle = 0
//        }
    }

    fun stopAnimation() {
        timer?.cancel()
        reset()
    }

    fun startTimer() {
        val START_DELAY = 2000L
        val INTERVAL = START_DELAY

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

    private fun reset() {

        firstContainer.get()?.clearAnimation()
        secondContainer.get()?.clearAnimation()

        firstContainer.get()?.alpha = 1f
        firstContainer.get()?.translationY = 0f
        secondContainer.get()?.alpha = 0f
        secondContainer.get()?.translationY = dpToPx(16)
        currentPositionSubtitle = 0

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
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -dpToPx(16))
                val translateAnimObjOne: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v1, translateAnimPropOne)

                val alphaAnimPropTwo = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                val alphaAnimObjTwo: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v2, alphaAnimPropTwo)

                val translateAnimPropTwo =
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dpToPx(16), 0f)
                val translateAnimObjTwo: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(v2, translateAnimPropTwo)

                translateAnimObjTwo.addListener(object: AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        Log.d("FEED_ANIM", "Animation End, Masuk")
                        currentPositionSubtitle += 1
                        if (currentPositionSubtitle >= subtitles.size) {
                            currentPositionSubtitle = 0
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                })

                animatorSet = AnimatorSet()
                animatorSet?.playTogether(
                    alphaAnimObjOne,
                    translateAnimObjOne,
                    alphaAnimObjTwo,
                    translateAnimObjTwo
                )
                animatorSet?.duration = 600L
                animatorSet?.start()
            }
        }
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}
