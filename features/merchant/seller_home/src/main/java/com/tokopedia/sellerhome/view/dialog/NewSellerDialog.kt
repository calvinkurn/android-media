package com.tokopedia.sellerhome.view.dialog

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel

/**
 * Created by @ilhamsuaib on 14/11/22.
 */

object NewSellerDialog {

    const val DISMISSAL_KEY: String = "widget.info.shopStateChanged"
    private const val IMG_WELCOMING_DIALOG =
        "https://images.tokopedia.net/img/android/seller_home/img_sah_new_seller_dialog.png"

    fun showFirstOrderDialog(context: Context, info: ShopStateInfoUiModel, onDismiss: () -> Unit) {
        val dialog = DialogUnify(
            context,
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )

        with(dialog) {
            setImageUrl(info.imageUrl)
            setTitle(info.title)
            setDescription(info.subtitle)
            setPrimaryCTAText(info.button.name)
            setSecondaryCTAText(info.buttonAlt.name)
            setPrimaryCTAClickListener {
                openAppLink(context, info.button.appLink)
                dismiss()
            }
            setSecondaryCTAClickListener {
                openAppLink(context, info.buttonAlt.appLink)
                dismiss()
            }
            setOnDismissListener(onDismiss)
            show()
        }
    }

    fun showNewSellerJourneyDialog(
        context: Context,
        shopName: String,
        onDismiss: () -> Unit
    ) {
        val dialog = DialogUnify(
            context,
            DialogUnify.SINGLE_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )

        with(dialog) {
            setImageUrl(IMG_WELCOMING_DIALOG)
            setTitle(
                context.getString(
                    R.string.sah_new_seller_welcoming_dialog_title,
                    shopName
                )
            )
            setDescription(context.getString(R.string.sah_new_seller_welcoming_dialog_description))
            setPrimaryCTAText(context.getString(R.string.sah_learn_a_new_look))
            setPrimaryCTAClickListener {
                dismiss()
            }
            setOnDismissListener(onDismiss)
            show()
        }
    }

    private fun openAppLink(context: Context, appLink: String) {
        if (appLink.isNotBlank()) {
            RouteManager.route(context, appLink)
        }
    }
}