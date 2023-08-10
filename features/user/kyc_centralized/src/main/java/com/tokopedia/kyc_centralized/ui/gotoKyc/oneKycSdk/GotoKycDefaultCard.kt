package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import android.view.View
import com.gojek.kyc.plus.card.KycPlusCard
import com.gojek.kyc.plus.card.KycPlusCardFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycBottomSheetBinding

class GotoKycDefaultCard : KycPlusCardFactory {
    override fun getDialogCard(
        activity: Activity,
        contentView: View,
        onDismiss: (() -> Unit)?
    ): KycPlusCard? {
        return if (activity.isFinishing) {
            null
        } else {
            val bottomSheetDialog = getBottomSheet(
                activity = activity,
                view = contentView
            )
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
            val bottomSheetDialog = getBottomSheet(
                activity = activity,
                view = contentView
            )
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
            val bottomSheetDialog = getBottomSheet(
                activity = activity,
                view = contentView,
                showCloseIcon = false
            )

            bottomSheetDialog.setCanceledOnTouchOutside(false)

            return GotoKycSdkBottomSheet(
                activity = activity,
                bottomSheetDialog = bottomSheetDialog,
                onDismiss = onUserDismiss
            )
        }
    }

    private fun getBottomSheet(activity: Activity, view: View, showCloseIcon: Boolean = true): BottomSheetDialog {
        val bottomSheet = BottomSheetDialog(activity, R.style.BottomSheetDialogStyle)

        val binding = LayoutGotoKycBottomSheetBinding.inflate(activity.layoutInflater)

        binding.root.addView(view)

        binding.icDismiss.setOnClickListener {
            bottomSheet.dismiss()
        }
        binding.spacing.showWithCondition(!showCloseIcon)
        binding.icDismiss.showWithCondition(showCloseIcon)

        bottomSheet.setContentView(binding.root)

        return bottomSheet
    }
}
