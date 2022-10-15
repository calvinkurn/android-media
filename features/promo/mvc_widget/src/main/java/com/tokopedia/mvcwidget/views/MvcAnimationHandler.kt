package com.tokopedia.mvcwidget.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.promoui.common.dpToPx
import java.lang.ref.WeakReference
import java.util.*

class MvcAnimationHandler(val firstContainer: WeakReference<MvcTextContainer>, val secondContainer: WeakReference<MvcTextContainer>) {
    lateinit var animatedInfoList: List<AnimatedInfos?>
    private var currentPositionAnimationInfo = 0

    private var visibleContainer = firstContainer
    private var invisibleContainer = secondContainer
    private var isAnimationStarted = false
    private var animatorSet:AnimatorSet?=null
    private var timer:Timer?=null
    var isTokomember = false

    private val animListener = object :Animator.AnimatorListener{
        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {
            afterAnimationComplete()
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animator) {

        }
    }

    private val onAttachListener = object :View.OnAttachStateChangeListener{
        override fun onViewAttachedToWindow(v: View?) {
            if (isTokomember){
                startTimer()
            }
        }

        override fun onViewDetachedFromWindow(v: View?) {
            if (isTokomember) {
                stopAnimation()
            }
        }
    }

    fun checkToCancelTimer(){
        firstContainer.get()?.addOnAttachStateChangeListener(onAttachListener)
    }

    private fun animateView() {
        if(isTokomember){
            isAnimationStarted = true

            setDataIntoViews()
            animateTwoViews(visibleContainer,invisibleContainer)
        }
    }

    private fun setDataIntoViews(){
        if (!::animatedInfoList.isInitialized) return

        val visibleDataPos = if (currentPositionAnimationInfo == animatedInfoList.size) 0 else currentPositionAnimationInfo
        val invisibleDataPos = if (currentPositionAnimationInfo + 1 >= animatedInfoList.size) 0 else currentPositionAnimationInfo + 1
        currentPositionAnimationInfo = visibleDataPos

        visibleContainer.get()?.setData(animatedInfoList[visibleDataPos])
        invisibleContainer.get()?.setData(animatedInfoList[invisibleDataPos])

    }

    fun stopAnimation(){
        timer?.cancel()
        reset()
    }

    fun startTimer(){
        val START_DELAY = 3000L
        val INTERVAL = START_DELAY

        if(timer == null){
            setDataIntoViews()
        }
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object :TimerTask(){
            override fun run() {
                visibleContainer.get()?.post {
                    animateView()
                }
            }
        },START_DELAY,INTERVAL)
    }

    private fun afterAnimationComplete(){
        if(!isTokomember) return
        if (!::animatedInfoList.isInitialized) return

        //switch container reference
        val tempContainer = visibleContainer
        visibleContainer = invisibleContainer
        invisibleContainer = tempContainer

        //increment pos
        currentPositionAnimationInfo += 1

        //reset position
        if (currentPositionAnimationInfo == animatedInfoList.size)
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
    private fun animateTwoViews(viewOne:WeakReference<MvcTextContainer>, viewTwo:WeakReference<MvcTextContainer>){

        viewOne.get()?.let {v1->


            viewTwo.get()?.let {v2->
                val alphaAnimPropOne = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
                val alphaAnimObjOne: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(v1, alphaAnimPropOne)

                val translateAnimPropOne = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -dpToPx(48))
                val translateAnimObjOne: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(v1, translateAnimPropOne)

                val alphaAnimPropTwo = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                val alphaAnimObjTwo: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(v2, alphaAnimPropTwo)

                val translateAnimPropTwo = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dpToPx(48),0f)
                val translateAnimObjTwo: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(v2, translateAnimPropTwo)
                translateAnimObjTwo.addListener(animListener)

                animatorSet = AnimatorSet()
                animatorSet?.playTogether(alphaAnimObjOne,translateAnimObjOne,alphaAnimObjTwo,translateAnimObjTwo)
                animatorSet?.duration = 600L
                animatorSet?.start()
            }
        }
    }
}
