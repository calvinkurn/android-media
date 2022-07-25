package com.tokopedia.campaign.utils.extension

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

private const val DEFAULT_SCROLL_ANIMATION_DURATION = 200

//region Toaster Error
fun View?.showToasterError(throwable: Throwable, ctaText: String = "") {
    if (this == null) return

    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
    showToasterError(errorMessage, ctaText)
}

fun View?.showToasterError(errorMessage: String, ctaText: String = "") {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToasterError(errorMessage)
    } else {
        showToasterErrorWithCta(errorMessage, ctaText)
    }
}
//endregion

//region Normal Toaster
fun View?.showToaster(message: String, ctaText: String = "") {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToaster(message)
    } else {
        showToasterWithCta(message, ctaText)
    }
}
//endregion

//region View Visibility Animation
fun View?.slideUp(duration: Int = DEFAULT_SCROLL_ANIMATION_DURATION) {
    this?.let {
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                visible()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        this.startAnimation(animate)
    }
}

fun View?.slideDown(duration: Int = DEFAULT_SCROLL_ANIMATION_DURATION) {
    this?.let {
        val animate =
            TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
               gone()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        this.startAnimation(animate)
    }
}
//endregion

//region View visibility
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
//endregion

//region View state
fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}
//endregion

//region Toaster internal
private fun View?.showToasterError(errorMessage: String) {
    Toaster.build(
        this ?: return,
        errorMessage,
        Toaster.LENGTH_SHORT,
        Toaster.TYPE_ERROR
    ).apply {
        anchorView = this@showToasterError
        show()
    }
}

private fun View?.showToasterErrorWithCta(errorMessage: String, ctaText: String) {
    Toaster.build(
        this ?: return,
        errorMessage,
        Toaster.LENGTH_SHORT,
        Toaster.TYPE_ERROR,
        ctaText
    ).apply {
        anchorView = this@showToasterErrorWithCta
        show()
    }
}

private fun View?.showToaster(message: String) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_NORMAL
    ).apply {
        anchorView = this@showToaster
        show()
    }
}

private fun View?.showToasterWithCta(message: String, ctaText: String) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_NORMAL,
        ctaText
    ).apply {
        anchorView = this@showToasterWithCta
        show()
    }
}
//endregion