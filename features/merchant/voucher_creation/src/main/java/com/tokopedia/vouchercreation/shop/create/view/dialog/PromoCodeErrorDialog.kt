package com.tokopedia.vouchercreation.shop.create.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.vouchercreation.R

class PromoCodeErrorDialog(context: Context,
                           private val onChangePromoCode: () -> Unit,
                           private val onBack: () -> Unit) {

    private val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)

    fun show() = with(dialog) {
        setTitle(context.getString(R.string.mvc_promo_code_error_title))
        setDescription(context.getString(R.string.mvc_promo_code_error_desc))
        setPrimaryCTAText(context.getString(R.string.mvc_promo_code_error_button))
        setPrimaryCTAClickListener {
            onChangePromoCode()
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.mvc_back))
        setSecondaryCTAClickListener {
            onBack()
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