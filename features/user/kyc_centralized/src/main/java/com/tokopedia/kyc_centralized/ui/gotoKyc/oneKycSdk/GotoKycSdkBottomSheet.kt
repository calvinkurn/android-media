package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.gojek.kyc.plus.card.KycPlusCard

class GotoKycSdkBottomSheet(
    private val activity: Activity,
    private val bottomSheetDialog: GotoKycBottomSheetDialog,
    private val onDismiss: (() -> Unit)?
) : KycPlusCard {

    override fun dismiss() {
        if (!activity.isFinishing) {
            bottomSheetDialog.dismiss()
        }
    }

    override fun isShowing(): Boolean {
        return if (!activity.isFinishing) {
            bottomSheetDialog.isVisible
        } else {
            false
        }
    }

    override fun show() {
        if (!activity.isFinishing && activity is AppCompatActivity) {
            bottomSheetDialog.show(activity.supportFragmentManager, GotoKycBottomSheetDialog.TAG)
            bottomSheetDialog.setOnDismissListener {
                onDismiss?.invoke()
            }
        }
    }

}
