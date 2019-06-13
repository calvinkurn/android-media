package com.tokopedia.topads.auto.view.widget

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.view.activity.DailyBudgetActivity

/**
 * Author errysuprayogi on 07,May,2019
 */
class SettingAutoAdsConfirmationSheet {

    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var activeAdsButton: View? = null
    private var nonActiveAutoAdsButton: View? = null
    private var actionListener: ActionListener? = null

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    private fun setupView(context: Context) {
        dialog!!.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }

        nonActiveAutoAdsButton!!.setOnClickListener { view ->
            actionListener?.nonActiveAutoAds()
        }

        activeAdsButton!!.setOnClickListener { view ->
            actionListener?.activeAutoAds()
        }

        closeButton!!.setOnClickListener { view -> dismissDialog() }
    }

    fun show() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    interface ActionListener {
        fun activeAutoAds()

        fun nonActiveAutoAds()
    }

    companion object {

        fun newInstance(context: Context): SettingAutoAdsConfirmationSheet {
            val fragment = SettingAutoAdsConfirmationSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.AutoAdsBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.layout_confirmation_setting_ads)
            fragment.closeButton = fragment.dialog!!.findViewById(R.id.btn_close)
            fragment.nonActiveAutoAdsButton = fragment.dialog!!.findViewById(R.id.btn_nonactive_ads)
            fragment.activeAdsButton = fragment.dialog!!.findViewById(R.id.btn_active_ads)
            fragment.setupView(context)
            return fragment
        }
    }
}
