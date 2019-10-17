package com.tokopedia.product.detail.view.widget

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.product.detail.R

/**
 * Author errysuprayogi on 07,May,2019
 */
class TopAdsDetailSheet {

    private var dialog: BottomSheetDialog? = null

    private fun setupView(context: Context) {
        dialog!!.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }

    }

    fun show() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    companion object {

        fun newInstance(context: Context): TopAdsDetailSheet {
            val fragment = TopAdsDetailSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.TopAdsDetailBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.pdp_topads_detail_sheet)
            fragment.setupView(context)
            return fragment
        }
    }
}
