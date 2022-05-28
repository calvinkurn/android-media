package com.tokopedia.shop.flash_sale.common.extension

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

infix fun View?.showError(throwable : Throwable) {
    val errorMessage = ErrorHandler.getErrorMessage(this?.context, throwable)
    Toaster.build(this ?: return, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
}

fun View?.slideUp(duration: Int = 350) {
    this?.let {
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                visible()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        this.startAnimation(animate)
    }

}

fun View?.slideDown(duration : Int = 350) {
    this?.let {
        val animate =
            TranslateAnimation(Float.ZERO, Float.ZERO, Float.ZERO, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                gone()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        this.startAnimation(animate)
    }
}
