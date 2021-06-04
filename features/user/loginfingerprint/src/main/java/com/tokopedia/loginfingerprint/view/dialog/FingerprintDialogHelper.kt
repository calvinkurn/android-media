package com.tokopedia.loginfingerprint.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.loginfingerprint.R

object FingerprintDialogHelper {
    fun showInvalidFingerprintDialog(context: Context?, onPositiveButtonClick: () -> Unit? = {}) {
        context?.run {
            val dialog = DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.error_fingerprint_invalid_title))
            dialog.setDescription(getString(R.string.error_fingerprint_invalid_description))
            dialog.setPrimaryCTAText(getString(R.string.error_fingerprint_use_other_method))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                onPositiveButtonClick()
            }
            dialog.show()
        }
    }
}