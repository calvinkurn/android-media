package com.tokopedia.shopdiscount.utils.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

infix fun View?.showError(throwable : Throwable) {
    val errorMessage = ErrorHandler.getErrorMessage(this?.context, throwable)
    Toaster.build(this ?: return, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
}

infix fun View?.showError(errorMessage : String) {
    Toaster.build(this ?: return, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
}

infix fun View?.showToaster(message : String) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_NORMAL
    ).show()
}
