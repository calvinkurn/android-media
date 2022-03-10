package com.tokopedia.sellerorder.reschedule_pickup.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.databinding.ReschedulePickupLoadingDialogBinding
import com.tokopedia.sellerorder.databinding.SomListBulkActionDialogBinding

class ReschedulePickupLoadingDialog(private val context: Context) {

    private var onDismissAction: (() -> Unit)? = null
    private var dialogUnify: DialogUnify? = null

    private var binding: ReschedulePickupLoadingDialogBinding? = null

    fun init() {
        binding = ReschedulePickupLoadingDialogBinding.inflate(LayoutInflater.from(context))
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

    fun getDialogUnify() = dialogUnify

    fun show() {
        dialogUnify?.show()
    }

    fun dismiss() {
        dialogUnify?.dismiss()
    }
}