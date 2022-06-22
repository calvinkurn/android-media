package com.tokopedia.shop.flashsale.presentation.creation.manage.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.seller_shop_flash_sale.R

class ProductDeleteDialog() {

    private var onPrimaryActionClick: () -> Unit = {}

    fun show(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(context.getString(R.string.deleteproduct_dialog_title))
        dialog.setDescription(context.getString(R.string.deleteproduct_dialog_desc))
        dialog.setPrimaryCTAText(context.getString(R.string.sfs_confirm_delete))
        dialog.setSecondaryCTAText(context.getString(R.string.sfs_no))

        dialog.setPrimaryCTAClickListener {
            onPrimaryActionClick.invoke()
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setOnPrimaryActionClick(onPrimaryActionClick: () -> Unit) {
        this.onPrimaryActionClick = onPrimaryActionClick
    }
}