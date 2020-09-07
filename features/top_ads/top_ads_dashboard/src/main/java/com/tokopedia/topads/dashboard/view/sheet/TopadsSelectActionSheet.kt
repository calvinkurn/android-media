package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import kotlinx.android.synthetic.main.topads_dash_select_action_on_group_bottomsheet.*

/**
 * Created by Pika on 3/6/20.
 */

class TopadsSelectActionSheet {
    private var dialog: BottomSheetDialog? = null
    var onDeleteClick: (() -> Unit)? = null
    var changeStatus: ((active: Int) -> Unit)? = null
    var onEditAction: (() -> Unit)? = null
    private var ACTIVATED = 1

    private fun setupView(context: Context, activeStatus: Int, name: String) {
        dialog?.let {
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                frameLayout?.let {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.btn_close.setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_group_close))
            it.img_active.setImageDrawable(context.getResDrawable(R.drawable.topads_dash_green_dot))
            it.edit_img.setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_edit_pen_icon))
            it.img_delete.setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_delete))
            it.btn_close.setOnClickListener {
                dismissDialog()
            }
            it.action_edit.setOnClickListener {
                onEditAction?.invoke()
                dismissDialog()
            }
            it.action_delete.setOnClickListener {
                showConfirmationDialog(context, name)
                dismissDialog()
            }
            it.txt_title.text = name
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

    private fun showConfirmationDialog(context: Context, name: String) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(String.format(context.getString(R.string.topads_dash_group_del_confirm), name))
        dialog.setPrimaryCTAText(context.getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
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

        fun newInstance(context: Context, activeStatus: Int, groupName: String): TopadsSelectActionSheet {
            val fragment = TopadsSelectActionSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_select_action_on_group_bottomsheet)
            fragment.setupView(context, activeStatus, groupName)
            return fragment
        }
    }
}