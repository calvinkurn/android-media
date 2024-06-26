package com.tokopedia.sellerorder.list.presentation.dialogs

import android.content.Context
import android.view.View
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.sellerorder.R as sellerorderR

class SomListBulkRequestPickupDialog(context: Context) {

    private var onDismissAction: (() -> Unit)? = null
    private var dialogUnify: DialogUnify? = null
    private var childViews: View? = null
    private var titleDialog: Typography? = null
    private var descDialog: Typography? = null
    private var btnPrimaryDialog: UnifyButton? = null
    private var btnSecondaryDialog: UnifyButton? = null
    private var loaderBulkAccept: LoaderUnify? = null
    private var ivBulkAcceptDialog: ImageUnify? = null

    init {
        childViews = View.inflate(
            context,
            sellerorderR.layout.som_list_bulk_action_dialog,
            null
        )
        dialogUnify =
            DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
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
        initChildView()
    }

    private fun initChildView() = childViews?.run {
        titleDialog = findViewById(sellerorderR.id.tvSomListBulkActionDialogTitle)
        descDialog =
            findViewById(sellerorderR.id.tvSomListBulkActionDialogDescription)
        btnPrimaryDialog =
            findViewById(sellerorderR.id.btnSomListBulkActionDialogPrimaryButton)
        btnSecondaryDialog =
            findViewById(sellerorderR.id.btnSomListBulkActionDialogSecondaryButton)
        loaderBulkAccept = findViewById(sellerorderR.id.loaderBulkAccept)
        ivBulkAcceptDialog = findViewById(sellerorderR.id.ivBulkAcceptDialog)
    }

    fun setOnDismiss(action: () -> Unit) {
        this.onDismissAction = action
    }

    fun refreshData() {
        onDismissAction?.invoke()
    }

    fun dismissAndRunAction() {
        dialogUnify?.dismiss()
        onDismissAction?.invoke()
    }

    fun show() {
        if (dialogUnify?.isShowing == false) {
            dialogUnify?.show()
        }
    }

    fun getDialogUnify() = dialogUnify

    fun dismiss() {
        dialogUnify?.dismiss()
    }

    fun setTitle(title: String) {
        titleDialog?.text = title
    }

    fun setDescription(description: String) {
        descDialog?.run {
            text = description
            show()
        }
    }

    fun showOnProgress() {
        ivBulkAcceptDialog?.hide()
        loaderBulkAccept?.show()
    }

    fun showSuccess() {
        loaderBulkAccept?.hide()
        ivBulkAcceptDialog?.apply {
            loadImage(sellerorderR.drawable.ic_som_bulk_success)
            show()
        }
    }

    fun showFailed() {
        loaderBulkAccept?.hide()
        ivBulkAcceptDialog?.apply {
            loadImage(sellerorderR.drawable.ic_som_bulk_fail)
            show()
        }
    }

    fun setPrimaryButton(text: String, onPrimaryButtonClicked: () -> Unit = {}) {
        btnPrimaryDialog?.apply {
            setText(text)
            showWithCondition(text.isNotBlank())
            setOnClickListener {
                onPrimaryButtonClicked.invoke()
            }
        }
    }

    fun setSecondaryButton(text: String, onSecondaryButtonClicked: () -> Unit = {}) {
        btnSecondaryDialog?.apply {
            setText(text)
            showWithCondition(text.isNotBlank())
            setOnClickListener {
                onSecondaryButtonClicked.invoke()
            }
        }
    }

    fun hidePrimaryButton() {
        btnPrimaryDialog?.hide()
    }

    fun hideSecondaryButton() {
        btnSecondaryDialog?.hide()
    }
}
