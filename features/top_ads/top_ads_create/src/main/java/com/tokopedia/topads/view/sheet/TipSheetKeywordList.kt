package com.tokopedia.topads.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import com.tokopedia.topads.create.R
import kotlinx.android.synthetic.main.topads_create_fragment_group_sheet_info.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class TipSheetKeywordList {

    private var dialog: BottomSheetDialog? = null

    private fun setupView(context: Context) {
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

        fun newInstance(context: Context): TipSheetKeywordList {
            val fragment = TipSheetKeywordList()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.topads_create_fragment_keyword_sheet_tip)
            fragment.setupView(context)
            return fragment
        }
    }
}
