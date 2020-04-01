package com.tokopedia.home_recom.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


fun View.fadeHide(duration: Int = 50){
    animate().alpha(0f).setDuration(duration.toLong()).setListener(object: AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            // reset alpha to 1
            visibility = View.GONE
        }
    })
}
fun View.fadeShow(duration: Int = 200){
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration.toLong()).setListener(null)
}

fun View.startFade(isShow: Boolean){
    if(isShow) this.fadeShow()
    else this.fadeHide()
}