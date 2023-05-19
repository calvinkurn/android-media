package com.tokopedia.shop.common.extension

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

fun Fragment.showToasterError(throwable: Throwable) {
    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)

    activity?.let {
        Toaster.build(view ?: return, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }
}

fun Fragment.showToaster(message: String) {
    activity?.let {
        Toaster.build(view ?: return, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
    }
}
