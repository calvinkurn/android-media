package com.tokopedia.sellerhome.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sellerhome.R

/**
 * Created by @ilhamsuaib on 14/11/22.
 */

object NewSellerFirstOrderDialog {

    private const val IMG_NEW_ORDER_DIALOG = "https://images.tokopedia.net/img/android/seller_home/img_sah_first_order_dialog.png"

    fun show(context: Context) {
        val dialog = DialogUnify(
            context,
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )

        with(dialog) {
            setImageUrl(IMG_NEW_ORDER_DIALOG)
            setTitle(context.getString(R.string.sah_new_seller_dialog_first_order_title))
            setDescription(context.getString(R.string.sah_new_seller_dialog_first_order_description))
            setPrimaryCTAText(context.getString(R.string.sah_process_order))
            setSecondaryCTAText(context.getString(R.string.sah_later))
            setPrimaryCTAClickListener {
                dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
            show()
        }
    }
}