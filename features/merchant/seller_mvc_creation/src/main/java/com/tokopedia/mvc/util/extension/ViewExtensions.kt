package com.tokopedia.mvc.util.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.unifycomponents.Toaster

fun View.showErrorToaster(errorMessage: String) {
    Toaster.build(this,
        errorMessage,
        Snackbar.LENGTH_SHORT,
        Toaster.TYPE_ERROR)
}
