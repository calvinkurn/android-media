package com.tokopedia.vouchercreation.common.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.unifycomponents.Toaster

fun View.showErrorToaster(errorMessage: String) {
    Toaster.make(this,
            errorMessage,
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_ERROR)
}