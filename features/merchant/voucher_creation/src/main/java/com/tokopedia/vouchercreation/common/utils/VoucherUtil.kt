package com.tokopedia.vouchercreation.common.utils

import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.vouchercreation.R

fun View.showErrorToaster(errorMessage: String) {
    Toaster.make(this,
            errorMessage,
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_ERROR)
}

fun View.showDownloadActionTicker(isSuccess: Boolean,
                                  isInternetProblem: Boolean = true) {
    val toasterType: Int
    val toasterMessage: String
    if (isSuccess) {
        toasterType = Toaster.TYPE_NORMAL
        toasterMessage = context?.getString(R.string.mvc_success_download_voucher).toBlankOrString()
    } else {
        toasterType = Toaster.TYPE_ERROR
        val errorMessageSuffix =
                if (isInternetProblem) {
                    context?.getString(R.string.mvc_fail_download_voucher_suffix).toBlankOrString()
                } else {
                    ""
                }
        toasterMessage = "${context?.getString(R.string.mvc_fail_download_voucher).toBlankOrString()}$errorMessageSuffix"
    }

    Toaster.make(this,
            toasterMessage,
            Toaster.LENGTH_LONG,
            toasterType,
            context?.getString(R.string.mvc_oke).toBlankOrString())
}

fun FragmentManager.dismissBottomSheetWithTags(vararg tags: String) {
    tags.forEach {
        (findFragmentByTag(it) as? BottomSheetUnify)?.dismiss()
    }
}