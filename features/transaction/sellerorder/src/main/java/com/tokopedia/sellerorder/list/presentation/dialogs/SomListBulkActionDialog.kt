package com.tokopedia.sellerorder.list.presentation.dialogs

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton



class SomListBulkActionDialog(private val context: Context) {

    companion object {
        private const val ILLUSTRATION_URL = "https://i.pinimg.com/originals/8f/d0/76/8fd0766a05487950538b79d29e9a66cb.gif"
    }

    private var dialogUnify: DialogUnify? = null
    private var childViews: View? = null
    private var imageIllustration: ImageUnify? = null

    fun init() {
        childViews = View.inflate(context, com.tokopedia.sellerorder.R.layout.som_list_bulk_action_dialog, null)
        Glide.with(context).asGif().load(ILLUSTRATION_URL).preload()
        imageIllustration = ImageUnify(context)
        dialogUnify = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            dialogPrimaryCTA.gone()
            dialogSecondaryCTA.gone()
            setOverlayClose(false)
            setCancelable(false)
            dialogImageContainer.removeAllViews()
            dialogImageContainer.addView(imageIllustration)
            setOnShowListener {
                imageIllustration?.let { Glide.with(context).asGif().load(ILLUSTRATION_URL).into(it) }
            }
            setChild(childViews)
        }
    }

    fun setTitle(title: String) {
        dialogUnify?.setTitle(title)
    }

    fun setDescription(description: String) {
        dialogUnify?.setDescription(description)
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

    fun show() {
        dialogUnify?.show()
    }

    fun dismiss() {
        dialogUnify?.dismiss()
    }

    fun hidePrimaryButton() {
        childViews?.findViewById<UnifyButton>(com.tokopedia.sellerorder.R.id.btnSomListBulkActionDialogPrimaryButton)?.gone()
    }

    fun hideSecondaryButton() {
        childViews?.findViewById<UnifyButton>(com.tokopedia.sellerorder.R.id.btnSomListBulkActionDialogSecondaryButton)?.gone()
    }
}