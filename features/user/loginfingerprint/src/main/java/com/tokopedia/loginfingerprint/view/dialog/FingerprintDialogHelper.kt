package com.tokopedia.loginfingerprint.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.loginfingerprint.R

object FingerprintDialogHelper {

    fun showFingerprintLockoutDialog(context: Context?, onPositiveButtonClick: () -> Unit? = {}, onDismiss: () -> Unit? = {}) {
        context?.run {
            val dialog = DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.error_fingerprint_invalid_title))
            dialog.setDescription(getString(R.string.error_fingerprint_lockout_description))
            dialog.setPrimaryCTAText(getString(R.string.error_fingerprint_ok))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                onPositiveButtonClick()
            }
            dialog.setOnDismissListener { onDismiss() }
            dialog.show()
        }
    }

    fun showNotRegisteredFingerprintDialog(context: Context?, onPositiveButtonClick: () -> Unit? = {}, onDismiss: () -> Unit? = {}) {
        context?.run {
            val dialog = DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.error_fingerprint_not_registered))
                setDescription(getString(R.string.error_fingerprint_not_registered_description))
                setPrimaryCTAText(getString(R.string.button_close_fingerprint))
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setPrimaryCTAClickListener {
                    dismiss()
                    onPositiveButtonClick()
                }
                setOnDismissListener { onDismiss() }
            }
            dialog.show()
        }
    }

}