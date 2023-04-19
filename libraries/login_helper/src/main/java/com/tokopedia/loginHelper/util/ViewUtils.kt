package com.tokopedia.loginHelper.util

import android.view.View
import com.tokopedia.unifycomponents.Toaster

fun View?.showToaster(message: String, ctaText: String = "", clickListener: (View) -> Unit = {}) {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToaster(message)
    } else {
        showToasterWithCta(message, ctaText, clickListener)
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

private fun View?.showToasterWithCta(
    message: String,
    ctaText: String,
    clickListener: (View) -> Unit
) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_NORMAL,
        ctaText,
        clickListener = clickListener
    ).apply {
        anchorView = this@showToasterWithCta
        show()
    }
}

fun View?.showToasterError(
    errorMessage: String,
    ctaText: String = "",
    clickListener: (View) -> Unit = {}
) {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToasterError(errorMessage)
    } else {
        showToasterErrorWithCta(errorMessage, ctaText, clickListener)
    }
}

private fun View?.showToasterError(message: String) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_ERROR
    ).apply {
        anchorView = this@showToasterError
        show()
    }
}

private fun View?.showToasterErrorWithCta(message: String, ctaText: String, clickListener: (View) -> Unit) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_ERROR,
        ctaText,
        clickListener = clickListener
    ).apply {
        anchorView = this@showToasterErrorWithCta
        show()
    }
}
