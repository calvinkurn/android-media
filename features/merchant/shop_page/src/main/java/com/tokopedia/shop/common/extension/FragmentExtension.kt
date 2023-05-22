package com.tokopedia.shop.common.extension

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

fun Fragment.showToasterError(view: View, throwable: Throwable) {
    if (!isAdded) return

    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
    Toaster.build(view, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
}

fun Fragment.showToaster(view: View, message: String) {
    if (!isAdded) return
    Toaster.build(view, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
}
