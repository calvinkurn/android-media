package com.tokopedia.topads.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.topads.create.R

/**
 * Author errysuprayogi on 07,May,2019
 */
class InfoSheetGroupList {

    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var agreeButton: View? = null

    private fun setupView(context: Context) {
        dialog!!.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }

        agreeButton!!.setOnClickListener { view -> dismissDialog() }

        closeButton!!.setOnClickListener { view -> dismissDialog() }
    }

    fun show() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    companion object {

        fun newInstance(context: Context): InfoSheetGroupList {
            val fragment = InfoSheetGroupList()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.topads_create_fragment_group_sheet_info)
            fragment.closeButton = fragment.dialog!!.findViewById(com.tokopedia.design.R.id.btn_close)
            fragment.setupView(context)
            return fragment
        }
    }
}
