package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import android.view.View
import com.gojek.kyc.plus.card.KycPlusCard
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycBottomSheetBinding

class GotoKycSdkBottomSheet(
    private val activity: Activity,
    private val view: View,
    private val onDismiss: (() -> Unit)?,
    private val showCloseIcon : Boolean = true
) : KycPlusCard {

    val bottomSheet = BottomSheetDialog(activity, R.style.BottomSheetDialogStyle)

    override fun dismiss() {
        bottomSheet.dismiss()
    }

    override fun isShowing(): Boolean {
        return bottomSheet.isShowing
    }

    override fun show() {
        val binding = LayoutGotoKycBottomSheetBinding.inflate(activity.layoutInflater)

        binding.root.addView(view)

        binding.icDismiss.setOnClickListener {
            bottomSheet.dismiss()
        }
        binding.spacing.showWithCondition(!showCloseIcon)
        binding.icDismiss.showWithCondition(showCloseIcon)

        bottomSheet.setContentView(binding.root)

        bottomSheet.show()

        bottomSheet.setOnDismissListener {
            onDismiss?.invoke()
        }
    }

}
