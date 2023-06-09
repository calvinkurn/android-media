package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import com.gojek.kyc.plus.card.KycPlusCard
import com.google.android.material.bottomsheet.BottomSheetDialog

class GotoKycSdkBottomSheet(
    private val activity: Activity,
    private val bottomSheetDialog: BottomSheetDialog,
    private val onDismiss: (() -> Unit)?
) : KycPlusCard {

    override fun dismiss() {
        if (!activity.isFinishing) {
            bottomSheetDialog.dismiss()
        }
    }

    override fun isShowing(): Boolean {
        return if (!activity.isFinishing) {
            bottomSheetDialog.isShowing
        } else {
            false
        }
    }

    override fun show() {
        if (!activity.isFinishing) {
            bottomSheetDialog.show()
            bottomSheetDialog.setOnDismissListener {
                onDismiss?.invoke()
            }
        }
    }

}
