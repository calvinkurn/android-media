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
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ReschedulePickupFailedDialogBinding
import com.tokopedia.sellerorder.databinding.ReschedulePickupLoadingDialogBinding
import com.tokopedia.sellerorder.databinding.SomListBulkActionDialogBinding

class ReschedulePickupFailedDialog(private val context: Context) {

    private var dialogUnify: DialogUnify? = null

    private var binding: ReschedulePickupFailedDialogBinding? = null

    fun init() {
        binding = ReschedulePickupFailedDialogBinding.inflate(LayoutInflater.from(context))
        binding?.ivFailedRpu?.setImageUrl(FAILED_RPU_ILLUSTRATION)
        dialogUnify = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            setOverlayClose(false)
            setCancelable(true)
            dialogImageContainer.removeAllViews()
            setChild(binding?.root)
            setPrimaryCTAText(context.getString(R.string.title_cta_error_reschedule_pickup))
            setPrimaryCTAClickListener {
                this.dismiss()
            }
            setOnDismissListener {
                binding = null
            }
        }
    }

    fun setErrorMessage(error: String) {
        binding?.tvDescriptionError?.text = error
    }

    fun getDialogUnify() = dialogUnify

    fun show() {
        dialogUnify?.show()
    }

    fun dismiss() {
        dialogUnify?.dismiss()
    }

    companion object {
        private const val FAILED_RPU_ILLUSTRATION = "https://images.tokopedia.net/img/android/rpu/failed_reschedule_pickup.png"
    }
}