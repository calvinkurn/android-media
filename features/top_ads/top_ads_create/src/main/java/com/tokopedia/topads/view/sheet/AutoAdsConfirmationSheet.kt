package com.tokopedia.topads.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds

import com.tokopedia.topads.create.R

class AutoAdsConfirmationSheet {

    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var cancel: View? = null
    private var startAutoAdsButton: View? = null

    private fun setupView(context: Context, autoClick: () -> Unit) {
        dialog!!.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }
        startAutoAdsButton?.setOnClickListener {
            RouteManager.route(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
            autoClick.invoke()
        }
        closeButton?.setOnClickListener { dismissDialog() }
        cancel?.setOnClickListener { dismissDialog() }

    }

    fun show() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    companion object {

        fun newInstance(context: Context, autoClick: () -> Unit): AutoAdsConfirmationSheet {
            val fragment = AutoAdsConfirmationSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.topads_create_bottom_sheet_layout_confirmation_auto_ads)
            fragment.closeButton = fragment.dialog!!.findViewById(com.tokopedia.design.R.id.btn_close)
            fragment.startAutoAdsButton = fragment.dialog!!.findViewById(R.id.btn_start_auto_ads)
            fragment.cancel = fragment.dialog!!.findViewById(R.id.cancel_btn_start_auto_ads)
            fragment.setupView(context,autoClick)
            return fragment
        }
    }
}
