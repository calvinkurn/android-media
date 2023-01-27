package com.tokopedia.chatbot.chatbot2.view.util

import android.view.View
import com.tokopedia.unifycomponents.Toaster

fun View?.showToaster(message: String, ctaText: String = "") {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToaster(message)
    } else {
        showToasterWithCta(message, ctaText)
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

fun View?.showToasterError(errorMessage: String, ctaText: String = "") {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToasterError(errorMessage)
    } else {
        showToasterErrorWithCta(errorMessage, ctaText)
    }
}

private fun View?.showToasterError(errorMessage: String) {
    Toaster.build(
        this ?: return,
        errorMessage,
        Toaster.LENGTH_SHORT,
        Toaster.TYPE_ERROR
    ).apply {
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
        show()
    }
}
