package com.tokopedia.sellerorder.list.presentation.dialogs

import android.content.Context
import android.view.View
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography


class SomListBulkAcceptOrderDialog(private val context: Context) {

    private var onDismissAction: (() -> Unit)? = null
    private var dialogUnify: DialogUnify? = null
    private var childViews: View? = null

    fun init() {
        childViews = View.inflate(context, com.tokopedia.sellerorder.R.layout.som_list_bulk_action_dialog, null)
        dialogUnify = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            dialogPrimaryCTA.gone()
            dialogSecondaryCTA.gone()
            setOverlayClose(false)
            setCancelable(false)
            dialogImageContainer.removeAllViews()
            setChild(childViews)
        }
    }

    fun setTitle(title: String) {
        childViews?.findViewById<Typography>(com.tokopedia.sellerorder.R.id.tvSomListBulkActionDialogTitle)?.text = title
    }

    fun setDescription(description: String) {
        childViews?.findViewById<Typography>(com.tokopedia.sellerorder.R.id.tvSomListBulkActionDialogDescription)?.text = description
    }

    fun showOnProgress() {
        childViews?.apply {
            findViewById<ImageUnify>(com.tokopedia.sellerorder.R.id.ivBulkAcceptDialog)?.gone()
            findViewById<LoaderUnify>(com.tokopedia.sellerorder.R.id.loaderBulkAccept)?.show()
        }
    }

    fun showSuccess() {
        childViews?.apply {
            findViewById<LoaderUnify>(com.tokopedia.sellerorder.R.id.loaderBulkAccept)?.gone()
            findViewById<ImageUnify>(com.tokopedia.sellerorder.R.id.ivBulkAcceptDialog)?.apply {
                loadImageDrawable(com.tokopedia.sellerorder.R.drawable.ic_som_list_success_bulk_accept)
                show()
            }
        }
    }

    fun showFailed() {
        childViews?.apply {
            findViewById<LoaderUnify>(com.tokopedia.sellerorder.R.id.loaderBulkAccept)?.gone()
            findViewById<ImageUnify>(com.tokopedia.sellerorder.R.id.ivBulkAcceptDialog)?.apply {
                loadImageDrawable(com.tokopedia.sellerorder.R.drawable.ic_som_list_failed_bulk_accept)
                show()
            }
        }
    }

    fun setPrimaryButton(text: String, onPrimaryButtomClicked: () -> Unit = {}) {
        childViews?.findViewById<UnifyButton>(com.tokopedia.sellerorder.R.id.btnSomListBulkActionDialogPrimaryButton)?.apply {
            setText(text)
            showWithCondition(text.isNotBlank())
            setOnClickListener {
                onPrimaryButtomClicked.invoke()
            }
        }
    }

    fun setSecondaryButton(text: String, onSecondaryButtomClicked: () -> Unit = {}) {
        childViews?.findViewById<UnifyButton>(com.tokopedia.sellerorder.R.id.btnSomListBulkActionDialogSecondaryButton)?.apply {
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
        childViews?.findViewById<UnifyButton>(com.tokopedia.sellerorder.R.id.btnSomListBulkActionDialogPrimaryButton)?.gone()
    }

    fun hideSecondaryButton() {
        childViews?.findViewById<UnifyButton>(com.tokopedia.sellerorder.R.id.btnSomListBulkActionDialogSecondaryButton)?.gone()
    }
}