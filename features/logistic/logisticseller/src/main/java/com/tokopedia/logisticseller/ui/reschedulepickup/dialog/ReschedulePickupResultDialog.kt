package com.tokopedia.logisticseller.ui.reschedulepickup.dialog

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.databinding.DialogResultReschedulePickupBinding
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ReschedulePickupResultDialog(private val context: Context, private val listener: ReschedulePickupResultDialogListener) {

    interface ReschedulePickupResultDialogListener {
        fun onClickDialog() {}
    }

    private var dialogUnify: DialogUnify? = null

    private var binding: DialogResultReschedulePickupBinding? = null

    fun init() {
        binding = DialogResultReschedulePickupBinding.inflate(LayoutInflater.from(context))
        dialogUnify = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            setOverlayClose(false)
            setCancelable(true)
            dialogImageContainer.removeAllViews()
            setChild(binding?.root)
            setPrimaryCTAClickListener {
                this.dismiss()
                listener.onClickDialog()
            }
            setOnDismissListener {
                binding = null
            }
        }
    }

    fun setErrorMessage(error: String) {
        binding?.run {
            titleDialogReschedule.text = context.getString(R.string.title_failed_reschedule_pickup_dialog)
            descriptionDialogReschedule.text = error
            ivReschedule.setImageResource(R.drawable.ic_logisticseller_reschedulepickup_fail)
        }
        dialogUnify?.setPrimaryCTAText(context.getString(R.string.title_cta_error_reschedule_pickup))
    }

    fun setSuccessMessage(message: String) {
        binding?.run {
            titleDialogReschedule.text = context.getString(R.string.title_reschedule_pickup_success_dialog)
            descriptionDialogReschedule.text = HtmlLinkHelper(context, message).spannedString
            ivReschedule.setImageResource(R.drawable.ic_logisticseller_recshedulepickup_success)
        }
        dialogUnify?.setPrimaryCTAText(context.getString(R.string.title_reschedule_pickup_button_dialog))
    }

    fun show() {
        dialogUnify?.show()
    }

    fun dismiss() {
        dialogUnify?.dismiss()
    }
}