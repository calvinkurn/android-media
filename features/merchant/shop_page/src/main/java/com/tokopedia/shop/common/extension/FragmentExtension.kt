package com.tokopedia.shop.common.extension

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

fun Fragment.showToasterError(view: View, throwable: Throwable, anchorView: View? = null) {
    if (!isAdded) return

    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
    Toaster.build(view, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).apply {
        this.anchorView = anchorView
        show()
    }
}

fun Fragment.showToaster(
    message: String,
    view: View,
    ctaText: String = "",
    onCtaClicked: () -> Unit,
    anchorView: View? = null
) {
    if (!isAdded) return
    Toaster.build(view, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, ctaText) {
        onCtaClicked.invoke()
    }.apply {
        this.anchorView = anchorView
        show()
    }
}
