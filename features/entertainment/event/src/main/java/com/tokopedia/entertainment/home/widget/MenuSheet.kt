package com.tokopedia.entertainment.home.widget

import android.content.Context
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.entertainment.R
import kotlinx.android.synthetic.main.ent_layout_bottom_sheet.*

/**
 * Author errysuprayogi on 07,February,2020
 */
class MenuSheet(val itemClickListener: ItemClickListener) {

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
            it.btn_close.setOnClickListener {
                dismissDialog()
            }
            it.txt_promo.setOnClickListener {
                itemClickListener.onMenuPromoClick()
            }
            it.txt_help.setOnClickListener {
                itemClickListener.onMenuHelpClick()
            }
            it.txt_transaction.setOnClickListener {
                itemClickListener.onMenuTransactionListClick()
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

        fun newInstance(context: Context, itemClickListener: ItemClickListener): MenuSheet {
            val fragment = MenuSheet(itemClickListener)
            fragment.dialog = BottomSheetDialog(context, R.style.MenuBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.ent_layout_bottom_sheet)
            fragment.setupView(context)
            return fragment
        }
    }

    interface ItemClickListener {
        fun onMenuPromoClick()
        fun onMenuHelpClick()
        fun onMenuTransactionListClick()
    }
}