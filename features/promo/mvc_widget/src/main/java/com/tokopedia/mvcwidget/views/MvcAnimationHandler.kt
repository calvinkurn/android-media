package com.tokopedia.mvcwidget.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.promoui.common.dpToPx
import java.util.*

class MvcAnimationHandler(val firstContainer: MvcTextContainer, val secondContainer: MvcTextContainer) {
    lateinit var animatedInfoList: List<AnimatedInfos?>
    var currentPositionAnimationInfo = 0

    var visibleContainer = firstContainer
    var invisibleContainer = secondContainer
    var isAnimationStarted = false
    var animatorSet:AnimatorSet?=null
    var timer:Timer?=null

    val animListener = object :Animator.AnimatorListener{
        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            afterAnimationComplete()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {

        }
    }

    fun animateView() {
        isAnimationStarted = true

        setDataIntoViews()
        animateTwoViews(visibleContainer,invisibleContainer)
    }

    fun setDataIntoViews(){
        if (!::animatedInfoList.isInitialized) return

        val visibleDataPos = if (currentPositionAnimationInfo == animatedInfoList.size) 0 else currentPositionAnimationInfo
        val invisibleDataPos = if (currentPositionAnimationInfo + 1 >= animatedInfoList.size) 0 else currentPositionAnimationInfo + 1
        currentPositionAnimationInfo = visibleDataPos

        visibleContainer.setData(animatedInfoList[visibleDataPos])
        invisibleContainer.setData(animatedInfoList[invisibleDataPos])

    }

    fun startTimer(){
        if(timer == null){
            setDataIntoViews()
        }
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object :TimerTask(){
            override fun run() {
                visibleContainer.post {
                    animateView()
                }
            }
        },3000L,3000L)
    }

    fun afterAnimationComplete(){
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

    fun reset() {

        firstContainer.clearAnimation()
        secondContainer.clearAnimation()

        firstContainer.alpha = 1f
        firstContainer.translationY = 0f
        secondContainer.alpha = 0f
        secondContainer.translationY = dpToPx(48)
        currentPositionAnimationInfo = 0

        visibleContainer = firstContainer
        invisibleContainer = secondContainer

        setDataIntoViews()
    }

    @SuppressLint("Recycle")
    fun animateTwoViews(viewOne:View, viewTwo:View){

        val alphaAnimPropOne = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
        val alphaAnimObjOne: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewOne, alphaAnimPropOne)

        val translateAnimPropOne = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -dpToPx(48))
        val translateAnimObjOne: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewOne, translateAnimPropOne)

        val alphaAnimPropTwo = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnimObjTwo: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewTwo, alphaAnimPropTwo)

        val translateAnimPropTwo = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dpToPx(48),0f)
        val translateAnimObjTwo: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewTwo, translateAnimPropTwo)
        translateAnimObjTwo.addListener(animListener)

        animatorSet = AnimatorSet()
        animatorSet?.playTogether(alphaAnimObjOne,translateAnimObjOne,alphaAnimObjTwo,translateAnimObjTwo)
        animatorSet?.duration = 600L
        animatorSet?.start()
    }

    fun slideUpFromMiddle(view: View, duration: Long = 600, completion: (() -> Unit)? = null) {
        view.postDelayed({
            view.animate()
                .alpha(1f)
                .setDuration(duration)
                .translationY(-dpToPx(48))
                .withEndAction {
                    view.translationY = -view.translationY
                    completion?.invoke()
                }
        }, 3000L)



    }

    fun slideUpFromBottom(view: View, duration: Long = 600, completion: (() -> Unit)? = null) {
        view.postDelayed({
            view.animate()
                .alpha(1f)
                .setDuration(duration)
                .translationY(0f)
                .withEndAction {
                    completion?.invoke()
                }
        }, 3000L)

    }
}