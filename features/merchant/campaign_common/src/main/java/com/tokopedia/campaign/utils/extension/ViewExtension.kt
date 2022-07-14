package com.tokopedia.campaign.utils.extension

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.tokopedia.campaign.R
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

private const val DEFAULT_SCROLL_ANIMATION_DURATION = 200

fun View?.showToasterError(throwable: Throwable, ctaText: String = "") {
    this?.run {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToasterError(errorMessage, ctaText)
    }
}

fun View?.showToasterError(errorMessage: String, ctaText: String = "") {
    if (ctaText.isEmpty()) {
        Toaster.build(
            this ?: return,
            errorMessage,
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_ERROR
        ).apply {
            anchorView = this@showToasterError
            show()
        }
    } else {
        val updatedCtaText = ctaText.ifEmpty { this?.context?.getString(R.string.campaign_common_oke) }.orEmpty()
        Toaster.build(
            this ?: return,
            errorMessage,
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_ERROR,
            updatedCtaText
        ).apply {
            anchorView = this@showToasterError
            show()
        }
    }
}

fun View?.showToaster(message: String, ctaText: String = "") {
    if (ctaText.isEmpty()) {
        Toaster.build(
            this ?: return,
            message,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL
        ).apply {
            anchorView = this@showToaster
            show()
        }
    } else {
        val updatedCtaText = ctaText.ifEmpty { this?.context?.getString(R.string.campaign_common_oke) }.orEmpty()
        Toaster.build(
            this ?: return,
            message,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL,
            updatedCtaText
        ).apply {
            anchorView = this@showToaster
            show()
        }
    }

}


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

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}
