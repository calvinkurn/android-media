package com.tokopedia.creation.common.upload.uploader.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.creation.common.R
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created By : Jonathan Darwin on May 03, 2023
 */
class ContentInstallMainAppDialog {

    companion object {
        private const val CUSTOMER_APP_PACKAGE = "com.tokopedia.tkpd"
        private const val CUSTOMER_APP_PLAY_STORE = "https://play.google.com/store/apps/details?id=$CUSTOMER_APP_PACKAGE"
    }

    private var dialog: DialogUnify? = null

    fun openPlayStore(
        context: Context,
        onDismiss: () -> Unit,
    ) {
        getDialog(context, onDismiss)?.show()
    }

    fun clear() {
        dialog?.dismiss()
        dialog = null
    }

    private fun getDialog(
        context: Context,
        onDismiss: () -> Unit,
    ): DialogUnify? {
        if (dialog == null) {
            dialog = DialogUnify(
                context = context,
                actionType = DialogUnify.VERTICAL_ACTION,
                imageType = DialogUnify.NO_IMAGE
            ).apply {
                setTitle(
                    context.getString(R.string.content_creation_watch_in_ma_title)
                )

                setDescription(
                    context.getString(R.string.content_creation_watch_in_ma_desc)
                )

                setPrimaryCTAText(
                    context.getString(R.string.content_creation_watch_in_ma_action_text)
                )

                setSecondaryCTAText(
                    context.getString(R.string.content_creation_watch_in_ma_cancel_text)
                )
            }
        }

        dialog?.dialogSecondaryLongCTA?.buttonVariant = UnifyButton.Variant.GHOST

        dialog?.setPrimaryCTAClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(CUSTOMER_APP_PLAY_STORE)))
        }
        dialog?.setSecondaryCTAClickListener {
            dialog?.dismiss()
            onDismiss()
        }

        dialog?.setOnDismissListener {
            onDismiss()
        }

        return dialog
    }
}
