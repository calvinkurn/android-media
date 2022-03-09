package com.tokopedia.vouchercreation.product.create.view.dialog

import android.content.Context
import android.graphics.Color
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.vouchercreation.R

class CreateProductCouponFailedDialog(
    context: Context,
    private val onTryAgain: () -> Unit,
    private val onRequestHelp: () -> Unit
) {

    private val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)

    fun show() = with(dialog) {
        setImageDrawable(R.drawable.ic_create_voucher_fail)
        dialogImageContainer.run {
            setCardBackgroundColor(Color.TRANSPARENT)
            cardElevation = 0f
        }
        setTitle(context.getString(R.string.error_message_create_coupon_product))
        setDescription(context.getString(R.string.error_message_create_coupon_description))
        setPrimaryCTAText(context.getString(R.string.mvc_try_again))
        setPrimaryCTAClickListener {
            onTryAgain()
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.mvc_help))
        setSecondaryCTAClickListener {
            onRequestHelp()
            dismiss()
        }
        with(dialogSecondaryLongCTA) {
            buttonVariant = UnifyButton.Variant.TEXT_ONLY
            buttonType = UnifyButton.Type.ALTERNATE
        }
        show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

}