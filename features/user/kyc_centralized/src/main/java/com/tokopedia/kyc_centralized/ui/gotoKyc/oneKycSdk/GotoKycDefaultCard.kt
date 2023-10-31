package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import android.view.View
import com.gojek.kyc.plus.card.KycPlusCard
import com.gojek.kyc.plus.card.KycPlusCardFactory

class GotoKycDefaultCard : KycPlusCardFactory {
    override fun getDialogCard(
        activity: Activity,
        contentView: View,
        onDismiss: (() -> Unit)?
    ): KycPlusCard? {
        return if (activity.isFinishing) {
            null
        } else {
            val bottomSheetDialog = GotoKycBottomSheetDialog.newInstance(true)
            bottomSheetDialog.setView(contentView)
            return GotoKycSdkBottomSheet(
                activity = activity,
                bottomSheetDialog = bottomSheetDialog,
                onDismiss = onDismiss
            )
        }
    }

    override fun getDismissibleNotchCard(
        activity: Activity,
        contentView: View,
        resizable: Boolean,
        onDismiss: (() -> Unit)?
    ): KycPlusCard? {
        return if (activity.isFinishing) {
            null
        } else {
            val bottomSheetDialog = GotoKycBottomSheetDialog.newInstance(true)
            bottomSheetDialog.setView(contentView)
            return GotoKycSdkBottomSheet(
                activity = activity,
                bottomSheetDialog = bottomSheetDialog,
                onDismiss = onDismiss
            )
        }
    }

    override fun getFixedCard(
        activity: Activity,
        contentView: View,
        isModal: Boolean,
        onUserDismiss: (() -> Unit)?
    ): KycPlusCard? {
        return if (activity.isFinishing) {
            null
        } else {
            val bottomSheetDialog = GotoKycBottomSheetDialog.newInstance(false)
            bottomSheetDialog.setView(contentView)
            return GotoKycSdkBottomSheet(
                activity = activity,
                bottomSheetDialog = bottomSheetDialog,
                onDismiss = onUserDismiss
            )
        }
    }
}
