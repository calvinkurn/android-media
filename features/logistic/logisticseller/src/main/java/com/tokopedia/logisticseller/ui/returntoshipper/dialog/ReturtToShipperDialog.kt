package com.tokopedia.logisticseller.ui.returntoshipper.dialog

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import com.tokopedia.logisticseller.R

class ReturtToShipperDialog(
    private val context: Context
) {

    fun showRtsConfirmationDialog(
        data: GetGeneralInfoRtsResponse.GeneralInfoRtsData,
        onPrimaryCTAClickListener: (() -> Unit),
        onSecondaryCTAClickListener: (() -> Unit),
        onDismissListener: (() -> Unit),
    ) {
        showDialog(
            title = data.title,
            description = data.description,
            imageUrl = data.urlImage,
            primaryCtaText = context.getString(R.string.btn_rts_request),
            secondaryCtaText = context.getString(R.string.btn_rts_help),
            dialogAction = DialogUnify.HORIZONTAL_ACTION,
            imageType = DialogUnify.WITH_ILLUSTRATION,
            onPrimaryCTAClickListener = {
                onPrimaryCTAClickListener.invoke()
            },
            onSecondaryCTAClickListener = {
                onSecondaryCTAClickListener.invoke()
            },
            onDismissListener = {
                onDismissListener.invoke()
            }
        )
    }

    fun showRtsSuccessDialog(
        onDismissListener: (() -> Unit),
    ) {
        showDialog(
            title = context.getString(R.string.title_rts_success_dialog),
            description = context.getString(R.string.description_rts_success_dialog),
            primaryCtaText = context.getString(R.string.btn_rts_understand),
            dialogAction = DialogUnify.SINGLE_ACTION,
            imageType = DialogUnify.WITH_ICON,
            imageIcon = R.drawable.ic_logisticseller_recshedulepickup_success,
            onPrimaryCTAClickListener = {
                onDismissListener.invoke()
            },
            onDismissListener = {
                onDismissListener.invoke()
            }
        )
    }

    fun showRtsFailedDialog(
        onDismissListener: (() -> Unit),
    ) {
        showDialog(
            title = context.getString(R.string.title_rts_failed_dialog),
            description = context.getString(R.string.description_rts_failed_dialog),
            primaryCtaText = context.getString(R.string.btn_rts_understand),
            dialogAction = DialogUnify.SINGLE_ACTION,
            imageType = DialogUnify.WITH_ICON,
            imageIcon = R.drawable.ic_logisticseller_reschedulepickup_fail,
            onPrimaryCTAClickListener = {
                onDismissListener.invoke()
            },
            onDismissListener = {
                onDismissListener.invoke()
            }
        )
    }

    private fun showDialog(
        title: String,
        description: String,
        primaryCtaText: String,
        secondaryCtaText: String? = null,
        dialogAction: Int = DialogUnify.HORIZONTAL_ACTION,
        imageType: Int = DialogUnify.NO_IMAGE,
        onPrimaryCTAClickListener: (() -> Unit)? = null,
        onSecondaryCTAClickListener: (() -> Unit)? = null,
        onDismissListener: (() -> Unit)? = null,
        imageUrl: String = "",
        imageIcon: Int = 0
    ) {
        var isFromCTAClickListener = false

        DialogUnify(context, dialogAction, imageType).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            setTitle(title)
            setDescription(description)
            setPrimaryCTAText(primaryCtaText)
            setSecondaryCTAText(secondaryCtaText.orEmpty())
            if (imageType == DialogUnify.WITH_ILLUSTRATION) {
                setImageUrl(imageUrl)
            } else {
                setImageDrawable(imageIcon)
            }
            setPrimaryCTAClickListener {
                isFromCTAClickListener = true
                onPrimaryCTAClickListener?.invoke()
                this.dismiss()
            }
            setSecondaryCTAClickListener {
                onSecondaryCTAClickListener?.invoke()
            }
            setOnDismissListener {
                if (isFromCTAClickListener.not()) {
                    onDismissListener?.invoke()
                }
            }
            show()
        }
    }
}
