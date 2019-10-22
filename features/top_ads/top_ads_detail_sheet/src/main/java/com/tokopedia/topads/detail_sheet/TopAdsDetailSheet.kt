package com.tokopedia.topads.detail_sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import kotlinx.android.synthetic.main.pdp_topads_detail_sheet.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class TopAdsDetailSheet {

    private var dialog: BottomSheetDialog? = null
    var editTopAdsClick: (() -> Unit)? = null

    private fun setupView(context: Context) {
        dialog!!.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }
        dialog?.let {
            it.action_to_topads_dashboard.setOnClickListener {
                RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
            }
            it.action_edit_ads.setOnClickListener{
                editTopAdsClick?.invoke()
            }
            it.toggle_switch_ads.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    true -> it.txt_active_status.setText(context.getString(R.string.aktif))
                    false -> it.txt_active_status.setText(context.getString(R.string.tidak_aktif))
                }
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
