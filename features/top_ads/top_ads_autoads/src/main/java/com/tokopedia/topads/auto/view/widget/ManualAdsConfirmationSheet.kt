package com.tokopedia.topads.auto.view.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.router.TopAdsAutoRouter
import com.tokopedia.topads.auto.view.activity.DailyBudgetActivity

/**
 * Author errysuprayogi on 07,May,2019
 */
class ManualAdsConfirmationSheet {

    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var startManualAdsButton: View? = null
    private var startAutoAdsButton: View? = null
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

        startAutoAdsButton!!.setOnClickListener { view ->
            if (context is DailyBudgetActivity && actionListener != null) {
                actionListener!!.onAutoAdsClicked()
            }
        }

        startManualAdsButton!!.setOnClickListener { view ->
            if (context is DailyBudgetActivity && actionListener != null) {
                actionListener!!.onManualAdsClicked()
            }
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
        fun onManualAdsClicked()

        fun onAutoAdsClicked()
    }

    companion object {

        fun newInstance(context: Context): ManualAdsConfirmationSheet {
            val fragment = ManualAdsConfirmationSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.AutoAdsBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.layout_confirmation_manual_ads)
            fragment.closeButton = fragment.dialog!!.findViewById(R.id.btn_close)
            fragment.startAutoAdsButton = fragment.dialog!!.findViewById(R.id.btn_start_auto)
            fragment.startManualAdsButton = fragment.dialog!!.findViewById(R.id.btn_start_manual)
            fragment.setupView(context)
            return fragment
        }
    }
}
