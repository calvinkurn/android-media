package com.tokopedia.sellerorder.list.presentation.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.databinding.SomListBulkActionDialogBinding

class SomListBulkAcceptOrderDialog(private val context: Context) {

    private var onDismissAction: (() -> Unit)? = null
    private var dialogUnify: DialogUnify? = null

    private var binding: SomListBulkActionDialogBinding? = null

    fun init() {
        binding = SomListBulkActionDialogBinding.inflate(LayoutInflater.from(context))
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

    fun setTitle(title: String) {
        binding?.tvSomListBulkActionDialogTitle?.text = title
    }

    fun setDescription(description: String) {
        binding?.tvSomListBulkActionDialogDescription?.text = description
    }

    fun showOnProgress() {
        binding?.run {
            ivBulkAcceptDialog.gone()
            loaderBulkAccept.show()
        }
    }

    fun showSuccess() {
        binding?.run {
            loaderBulkAccept.gone()
            ivBulkAcceptDialog.apply {
                loadImage(com.tokopedia.sellerorder.R.drawable.ic_som_bulk_success)
                show()
            }
        }
    }

    fun showFailed() {
        binding?.run {
            loaderBulkAccept.gone()
            ivBulkAcceptDialog.apply {
                loadImage(com.tokopedia.sellerorder.R.drawable.ic_som_bulk_fail)
                show()
            }
        }
    }

    fun setPrimaryButton(text: String, onPrimaryButtomClicked: () -> Unit = {}) {
        binding?.btnSomListBulkActionDialogPrimaryButton?.run {
            setText(text)
            showWithCondition(text.isNotBlank())
            setOnClickListener {
                onPrimaryButtomClicked.invoke()
            }
        }
    }

    fun setSecondaryButton(text: String, onSecondaryButtomClicked: () -> Unit = {}) {
        binding?.btnSomListBulkActionDialogSecondaryButton?.run {
            setText(text)
            showWithCondition(text.isNotBlank())
            setOnClickListener {
                onSecondaryButtomClicked.invoke()
            }
        }
    }

    fun setOnDismiss(action: () -> Unit) {
        this.onDismissAction = action
    }

    fun show() {
        dialogUnify?.show()
    }

    fun dismiss() {
        dialogUnify?.dismiss()
    }

    fun dismissAndRunAction() {
        dialogUnify?.dismiss()
        onDismissAction?.invoke()
    }

    fun hidePrimaryButton() {
        binding?.btnSomListBulkActionDialogPrimaryButton?.gone()
    }

    fun hideSecondaryButton() {
        binding?.btnSomListBulkActionDialogSecondaryButton?.gone()
    }
}