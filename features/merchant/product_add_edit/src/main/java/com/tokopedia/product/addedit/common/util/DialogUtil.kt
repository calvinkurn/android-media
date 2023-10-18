package com.tokopedia.product.addedit.common.util

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.product.addedit.R

object DialogUtil {

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

    fun showDTVariantDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(context.getString(R.string.product_add_edit_text_title_variant_dt_can_not_delete))
            setDescription(
                context.getString(R.string.product_add_edit_text_description_variant_dt_can_not_delete))
            setPrimaryCTAText(context.getString(R.string.action_oke))
            setPrimaryCTAClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showSingleProductVariantDialog(context: Context, onConfirmed: () -> Unit) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.apply {
            setTitle(context.getString(R.string.product_add_edit_single_product_variant_dialog_title))
            setDescription(context.getString(R.string.product_add_edit_single_product_variant_dialog_desc))
            setPrimaryCTAText(context.getString(R.string.product_add_edit_single_product_variant_dialog_delete))
            setSecondaryCTAText(context.getString(R.string.action_back))
            setImageUrl(TokopediaImageUrl.AEP_SINGLE_VARIANT_WARNING)
            setPrimaryCTAClickListener {
                onConfirmed.invoke()
                dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
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
