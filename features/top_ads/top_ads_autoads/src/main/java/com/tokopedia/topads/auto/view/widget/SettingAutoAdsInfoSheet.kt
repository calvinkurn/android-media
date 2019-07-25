package com.tokopedia.topads.auto.view.widget

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.topads.auto.R

/**
 * Author errysuprayogi on 07,May,2019
 */
class SettingAutoAdsInfoSheet {

    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var gotItButton: View? = null
    private var agreeButton: View? = null

    private fun setupView(context: Context) {
        dialog!!.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }

        gotItButton!!.setOnClickListener { view -> dismissDialog() }

        closeButton!!.setOnClickListener { view -> dismissDialog() }
    }

    fun show() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    companion object {

        fun newInstance(context: Context): SettingAutoAdsInfoSheet {
            val fragment = SettingAutoAdsInfoSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.AutoAdsBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.layout_setting_daily_budget_ads_info)
            fragment.closeButton = fragment.dialog!!.findViewById(R.id.btn_close)
            fragment.gotItButton = fragment.dialog!!.findViewById(R.id.btn_got_it)
            fragment.setupView(context)
            return fragment
        }
    }
}
