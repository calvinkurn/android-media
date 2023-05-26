package com.tokopedia.shop.common.extension

import android.content.res.Configuration
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

fun Fragment.showToasterError(view: View, throwable: Throwable) {
    if (!isAdded) return

    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
    Toaster.build(view.rootView, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).apply {
        anchorView = view
        show()
    }
}

fun Fragment.showToaster(view: View, message: String) {
    if (!isAdded) return
    Toaster.build(view.rootView, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL).apply {
        anchorView = view
        show()
    }
}

fun Fragment.isOnDarkMode(): Boolean {
    return when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> false
    }
}
