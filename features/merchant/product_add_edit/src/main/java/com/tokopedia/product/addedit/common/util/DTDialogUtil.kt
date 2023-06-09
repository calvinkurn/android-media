package com.tokopedia.product.addedit.common.util

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.product.addedit.R

object DTDialogUtil {

    fun showDTStockDialog(context: Context, action: UserAction) {
        val dialog = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(context.getString(R.string.product_add_edit_text_product_dt_can_not_delete))
            setDescription(
                context.getString(R.string.product_add_edit_text_description_product_dt_can_not_delete,
                    getUserActionWording(context, action))
            )
            setPrimaryCTAText(context.getString(R.string.action_oke_got_it))
            setPrimaryCTAClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun getUserActionWording(context: Context, userAction: UserAction): String {
        return context.getString(
            when(userAction) {
                UserAction.REMOVE -> R.string.product_add_edit_dt_action_remove
                UserAction.ADD -> R.string.product_add_edit_dt_action_add
                UserAction.EDIT -> R.string.product_add_edit_dt_action_edit
                UserAction.ADD_VARIANT_TYPE -> R.string.product_add_edit_dt_action_add_variant_type
                UserAction.EDIT_VARIANT_TYPE -> R.string.product_add_edit_dt_action_edit_variant_type
            }
        )
    }

    enum class UserAction {
        REMOVE,
        ADD,
        EDIT,
        ADD_VARIANT_TYPE,
        EDIT_VARIANT_TYPE
    }

}
