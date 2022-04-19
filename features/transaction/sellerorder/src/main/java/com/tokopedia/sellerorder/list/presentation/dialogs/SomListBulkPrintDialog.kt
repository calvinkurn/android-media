package com.tokopedia.sellerorder.list.presentation.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.databinding.DialogMultiPrintAwbBinding
import com.tokopedia.user.session.UserSession

class SomListBulkPrintDialog(context: Context) {

    private var dialogUnify: DialogUnify? = null
    private var listener: SomListBulkPrintDialogClickListener? = null

    private var binding: DialogMultiPrintAwbBinding? = null

    init {
        binding = DialogMultiPrintAwbBinding.inflate(LayoutInflater.from(context))
        dialogUnify = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            dialogPrimaryCTA.gone()
            dialogSecondaryCTA.gone()
            dialogSecondaryLongCTA.gone()
            dialogCTAContainer.gone()
            setUnlockVersion()
            setOverlayClose(false)
            setCancelable(false)
            setChild(binding?.root)
            setOnDismissListener {
                binding = null
            }
            setOnShowListener {
                binding?.btnSomListBulkPrintDialogPrimaryButton?.setOnClickListener {
                    listener?.onPrintButtonClicked(binding?.cbMarkAsPrinted?.isChecked.orFalse())
                    dismiss()
                }
                binding?.btnSomListBulkPrintDialogSecondaryButton?.setOnClickListener {
                    val userSession = UserSession(context)
                    SomAnalytics.eventClickCancelOnBulkPrintAwb(userSession.userId)
                    dismiss()
                }
            }
        }
    }

    fun setTitle(title: String) {
        binding?.tvSomListBulkPrintDialogTitle?.text = title
    }

    fun setListener(listener: SomListBulkPrintDialogClickListener) {
        this.listener = listener
    }

    fun show() {
        dialogUnify?.show()
    }

    interface SomListBulkPrintDialogClickListener {
        fun onPrintButtonClicked(markAsPrinted: Boolean)
    }
}