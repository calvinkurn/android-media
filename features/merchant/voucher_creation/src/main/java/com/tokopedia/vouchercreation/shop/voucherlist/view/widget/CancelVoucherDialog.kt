package com.tokopedia.vouchercreation.shop.voucherlist.view.widget

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel

/**
 * Created By @ilhamsuaib on 27/04/20
 */

class CancelVoucherDialog(context: Context) {

    private val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
    private var primaryClick: () -> Unit = {}

    fun setOnPrimaryClickListener(callback: () -> Unit): CancelVoucherDialog {
        this.primaryClick = callback
        return this
    }

    fun show(voucher: VoucherUiModel) = with(dialog) {
        setTitle(context.getString(R.string.mvc_cancel_voucher_dialog_title))
        val description = context.getString(R.string.mvc_cancel_voucher_dialog_description)
        setDescription(String.format(description, voucher.name).parseAsHtml())
        setPrimaryCTAText(context.getString(R.string.mvc_yes_cancel))
        setPrimaryCTAClickListener {
            primaryClick()
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.mvc_back))
        setSecondaryCTAClickListener {
            dismiss()
        }
        with(dialogSecondaryCTA) {
            buttonVariant = UnifyButton.Variant.TEXT_ONLY
            buttonType = UnifyButton.Type.ALTERNATE
        }
        show()
    }
}