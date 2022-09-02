package com.tokopedia.logisticseller.ui.reschedulepickup.dialog

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.logisticseller.databinding.DialogLoadingReschedulePickupBinding

class ReschedulePickupLoadingDialog(private val context: Context) {

    private var dialogUnify: DialogUnify? = null

    private var binding: DialogLoadingReschedulePickupBinding? = null

    fun init() {
        binding = DialogLoadingReschedulePickupBinding.inflate(LayoutInflater.from(context))
        dialogUnify = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            dialogPrimaryCTA.gone()
            dialogSecondaryCTA.gone()
            setOverlayClose(false)
            setCancelable(false)
            dialogImageContainer.removeAllViews()
            setChild(binding?.root)
            setOnDismissListener {
                binding = null
            }
        }
    }


    fun show() {
        dialogUnify?.show()
    }

    fun dismiss() {
        dialogUnify?.dismiss()
    }
}