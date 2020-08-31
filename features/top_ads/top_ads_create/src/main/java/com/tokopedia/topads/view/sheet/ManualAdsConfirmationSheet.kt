package com.tokopedia.topads.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout

import com.tokopedia.topads.create.R

class ManualAdsConfirmationSheet {

    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var cancel: View? = null
    private var startManualAdsButton: View? = null

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
            dismissDialog()
            manualClick.invoke()
        }
        closeButton?.setOnClickListener { dismissDialog() }
        cancel?.setOnClickListener { dismissDialog() }
    }

    fun show() {
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    companion object {

        fun newInstance(context: Context, manualClick: () -> Unit): ManualAdsConfirmationSheet {
            val fragment = ManualAdsConfirmationSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_create_bottom_sheet_layout_confirmation_manual_ads)
            fragment.closeButton = fragment.dialog?.findViewById(com.tokopedia.design.R.id.btn_close)
            fragment.startManualAdsButton = fragment.dialog?.findViewById(R.id.btn_start_manual_ads)
            fragment.cancel = fragment.dialog?.findViewById(R.id.cancel_btn_start_manual_ads)
            fragment.setupView(manualClick)
            return fragment
        }
    }
}
