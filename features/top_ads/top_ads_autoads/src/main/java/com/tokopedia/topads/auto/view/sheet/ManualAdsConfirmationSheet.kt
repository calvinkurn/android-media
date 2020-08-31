package com.tokopedia.topads.auto.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.applink.AppUtil
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.auto.R

class ManualAdsConfirmationSheet {

    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var cancel: View? = null
    private var startManualAdsButton: View? = null
    var dismissed: (() -> Unit)? = null
    private var manualAdsBtnClicked = false

    private fun setupView(manualClick: () -> Unit) {
        dialog?.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }
        startManualAdsButton?.setOnClickListener {
            manualAdsBtnClicked = true
            dialog?.dismiss()
            manualClick.invoke()
        }
        closeButton?.setOnClickListener { dismissDialog() }
        cancel?.setOnClickListener { dismissDialog() }
        dialog?.setOnDismissListener {
            if(!manualAdsBtnClicked) {
                dismissDialog()
                manualAdsBtnClicked = false
            }
        }
    }

    fun show() {
        dialog?.show()
    }

    fun dismissDialog() {
        dismissed?.invoke()
        dialog?.dismiss()
    }

    companion object {

        fun newInstance(context: Context, manualClick: () -> Unit): ManualAdsConfirmationSheet {
            val fragment = ManualAdsConfirmationSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_autoads_bottom_sheet_layout_confirmation_manual_ads)
            fragment.closeButton = fragment.dialog?.findViewById(com.tokopedia.design.R.id.btn_close)
            fragment.startManualAdsButton = fragment.dialog?.findViewById(R.id.btn_start_manual_ads)
            fragment.cancel = fragment.dialog?.findViewById(R.id.cancel_btn_start_manual_ads)
            fragment.setupView(manualClick)
            return fragment
        }
    }
}
