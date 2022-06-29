package com.tokopedia.shop.flashsale.common.extension

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.Guideline
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException


infix fun View?.showError(throwable: Throwable) {
    this?.run {
        val errorMessage = context.getString(R.string.sfs_error_message_connection_error)
        val modifiedErrorMessage = when (throwable) {
            is UnknownHostException -> errorMessage
            is SocketTimeoutException -> errorMessage
            else -> ErrorHandler.getErrorMessage(context, throwable)
        }
        showError(modifiedErrorMessage)
    }
}

infix fun View?.showError(errorMessage: String) {
    Toaster.build(
        this ?: return,
        errorMessage,
        Toaster.LENGTH_SHORT,
        Toaster.TYPE_ERROR,
        this.context.getString(R.string.action_oke)
    ).apply {
        anchorView = this@showError
        show()
    }
}

fun View?.showErrorWithCta(errorMessage: String, ctaText: String, cta: (() -> Unit)? = null) {
    Toaster.build(
        this ?: return,
        errorMessage,
        Toaster.LENGTH_SHORT,
        Toaster.TYPE_ERROR,
        ctaText
    ) { cta?.invoke() }
        .show()
}

infix fun View?.showToaster(message: String) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_NORMAL,
        this.context.getString(R.string.action_oke)
    ).apply {
        anchorView = this@showToaster
        show()
    }
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
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

fun View?.slideDown(duration: Int = 350) {
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

fun View.enableChildren(enable: Boolean) {
    (this as? ViewGroup)?.let {
        for (i in Int.ZERO..it.childCount) {
            enableChildren(enable)
        }
    }
    isEnabled = enable
}

fun disableEnableControls(enable: Boolean, vg: ViewGroup?) {
    if (vg == null) return
    for (i in Int.ZERO until vg.childCount) {
        val child = vg.getChildAt(i)
        child.isEnabled = enable
        if (child is ViewGroup) {
            disableEnableControls(enable, child)
        }
    }
}

fun <T> debounce(
    waitMs: Long,
    scope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

fun Guideline?.animateSlide(fromHeight: Int, targetHeight: Int, isGuidelineBegin: Boolean) =
    this?.run {
        val valueAnimator = ValueAnimator.ofInt(fromHeight, targetHeight)
        valueAnimator.duration =
            resources.getInteger(com.tokopedia.unifyprinciples.R.integer.Unify_T2).toLong()
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            if (isGuidelineBegin) {
                setGuidelineBegin(animation.animatedValue as Int)
            } else {
                setGuidelineEnd(animation.animatedValue as Int)
            }
        }
        valueAnimator.start()
    }
