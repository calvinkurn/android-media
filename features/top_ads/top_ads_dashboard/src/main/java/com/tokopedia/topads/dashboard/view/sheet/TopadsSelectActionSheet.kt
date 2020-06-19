package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import kotlinx.android.synthetic.main.topads_dash_select_action_on_group_bottomsheet.*

/**
 * Created by Pika on 3/6/20.
 */

class TopadsSelectActionSheet {
    private var dialog: BottomSheetDialog? = null
    var onDeleteClick: (() -> Unit)? = null
    var changeStatus: ((active: Int) -> Unit)? = null
    private var ACTIVATED = 1

    private fun setupView(context: Context, activeStatus: Int, hasGroup: Boolean, groupName: String, groupId: Int, groupStatusDesc: String, adPriceBid: Int, adPriceDaily: Int) {
        dialog?.let {
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                frameLayout?.let {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.btn_close.setOnClickListener {
                dismissDialog()
            }
            it.action_edit.setOnClickListener {

                if (hasGroup) {
                    val bundle = Bundle()
                    bundle.putString(TopAdsDashboardConstant.groupId, groupId.toString())
                    bundle.putString(TopAdsDashboardConstant.groupName, groupName)
                    bundle.putString(TopAdsDashboardConstant.groupStatus, groupStatusDesc)
                    RouteManager.route(context, bundle, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)
                } else {
                    val bundle = Bundle()
                    bundle.putInt(TopAdsDashboardConstant.groupId, groupId)
                    bundle.putInt(TopAdsDashboardConstant.priceBid, adPriceBid)
                    bundle.putInt(TopAdsDashboardConstant.priceDaily, adPriceDaily)
                    bundle.putString(groupName, groupName)
                    RouteManager.route(context, bundle, ApplinkConstInternalTopAds.TOPADS_EDIT_WITHOUT_GROUP)
                }
                dismissDialog()
            }
            it.action_delete.setOnClickListener {
                showConfirmationDialog(context)
                dismissDialog()
            }
            if (activeStatus != ACTIVATED) {
                it.txt_action_active.text = context.getString(R.string.topads_dash_aktifan)
                it.img_active.setImageDrawable(context.getResDrawable(R.drawable.topads_dash_green_dot))

            } else {
                it.txt_action_active.text = context.getString(R.string.topads_dash_nonaktifkan)
                it.img_active.setImageDrawable(context.getResDrawable(R.drawable.topads_dash_grey_dot))
            }

            it.action_activate.setOnClickListener {
                changeStatus?.invoke(activeStatus)
                dismissDialog()
            }
        }
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(context.getString(R.string.topads_dash_group_del_confirm))
        dialog.setPrimaryCTAText(context.getString(R.string.topads_common_cancel_btn))
        dialog.setSecondaryCTAText(context.getString(R.string.topads_dash_ya_hapus))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            onDeleteClick?.invoke()
        }
        dialog.show()
    }

    fun show() {
        dialog?.show()
    }

    private fun dismissDialog() {
        dialog?.dismiss()
    }

    companion object {

        fun newInstance(context: Context, activeStatus: Int, hasGroup: Boolean,
                        groupName: String, groupId: Int, groupStatusDesc: String,
                        adPriceBid: Int, adPriceDaily: Int): TopadsSelectActionSheet {
            val fragment = TopadsSelectActionSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_select_action_on_group_bottomsheet)
            fragment.setupView(context, activeStatus, hasGroup, groupName, groupId, groupStatusDesc, adPriceBid, adPriceDaily)
            return fragment
        }
    }
}