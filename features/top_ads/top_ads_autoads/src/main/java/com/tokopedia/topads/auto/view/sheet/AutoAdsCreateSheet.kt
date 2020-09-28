package com.tokopedia.topads.auto.view.sheet

import android.content.Context
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.topads.auto.R
import kotlinx.android.synthetic.main.topads_autoads_fragment_budget_sheet_tip.*


class AutoAdsCreateSheet {

    private var dialog: BottomSheetDialog? = null

    private fun setupView() {
        dialog?.let {
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                if (frameLayout != null) {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.btn_close.setOnClickListener { dismissDialog() }
        }
    }

    fun show() {
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    companion object {

        fun newInstance(context: Context): AutoAdsCreateSheet {
            val fragment = AutoAdsCreateSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_autoads_fragment_budget_sheet_tip)
            fragment.setupView()
            return fragment
        }
    }
}
