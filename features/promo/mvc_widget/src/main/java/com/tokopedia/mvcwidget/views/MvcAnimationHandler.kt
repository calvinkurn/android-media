package com.tokopedia.mvcwidget.views

import android.animation.ValueAnimator
import android.view.View
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.promoui.common.dpToPx

class MvcAnimationHandler(val firstContainer: MvcTextContainer, val secondContainer: MvcTextContainer) {
    lateinit var  animatedInfoList: List<AnimatedInfos?>


    var currentPositionAnimationInfo = 0

    var visibleContainer = firstContainer
    var invisibleContainer = secondContainer

    fun animateView() {
        if(!::animatedInfoList.isInitialized) return

        val visibleDataPos =  if (currentPositionAnimationInfo  == animatedInfoList.size) 0 else currentPositionAnimationInfo
        val invisibleDataPos = if(currentPositionAnimationInfo + 1 >= animatedInfoList.size) 0 else currentPositionAnimationInfo + 1
        currentPositionAnimationInfo = visibleDataPos

        visibleContainer.setData(animatedInfoList[visibleDataPos])
        invisibleContainer.setData(animatedInfoList[invisibleDataPos])

        slideUpFromMiddle(visibleContainer)
        slideUpFromBottom(invisibleContainer,completion = {

            //switch container reference
            val tempContainer = visibleContainer
            visibleContainer = invisibleContainer
            invisibleContainer = tempContainer

            //increment pos
            currentPositionAnimationInfo += 1

            //reset position
            if(currentPositionAnimationInfo == animatedInfoList.size)
                currentPositionAnimationInfo = 0


            animateView()
        })
    }

    fun slideUpFromMiddle(view: View, duration: Long = 2000, completion: (() -> Unit)? = null) {
//        val anim = ValueAnimator.ofFloat(1f,0f)
//        anim.addUpdateListener {
//            val currentValue = it.animatedValue as Float
//            view.alpha = currentValue
//            view.translationY
//        }
        view.animate()
                .alpha(1f)
                .setDuration(duration)
                .translationY(-dpToPx(48))
                .withEndAction {
                    view.translationY = -view.translationY
                    completion?.invoke()
                }
    }

    fun slideUpFromBottom(view: View, duration: Long = 2000, completion: (() -> Unit)? = null) {
        view.animate()
                .alpha(1f)
                .setDuration(duration)
                .translationY(0f)
                .withEndAction {
                    view.postDelayed({
                        completion?.invoke()
                    }, 300L)
                }
    }
}