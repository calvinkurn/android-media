package com.tokopedia.shop.flashsale.presentation.creation.information.dialog

import android.content.Context
import android.view.View
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.UnifyButton

class CancelEditCampaignConfirmationDialog(private val context: Context) {

    private var onPrimaryActionClick: () -> Unit = {}
    private var onSecondaryActionClick: () -> Unit = {}
    private var onThirdActionClick: () -> Unit = {}

    fun show() {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        val view = View.inflate(context, R.layout.ssfs_dialog_cancel_edit_campaign_confirmation, null)
        dialog.setUnlockVersion()
        dialog.setChild(view)
        setupView(view, dialog)
        dialog.show()
    }

    private fun setupView(view: View, dialog: DialogUnify) {
        val btnPrimaryAction = view.findViewById<UnifyButton>(R.id.btnYes)
        val btnSecondaryAction = view.findViewById<UnifyButton>(R.id.btnNo)
        val btnThirdAction = view.findViewById<UnifyButton>(R.id.btnBack)

        btnPrimaryAction?.setOnClickListener {
            onPrimaryActionClick()
            dialog.dismiss()
        }
        btnSecondaryAction?.setOnClickListener {
            onSecondaryActionClick()
            dialog.dismiss()
        }
        btnThirdAction?.setOnClickListener {
            onThirdActionClick()
            dialog.dismiss()
        }
    }

    fun setOnPrimaryActionClick(onPrimaryActionClick: () -> Unit) {
        this.onPrimaryActionClick = onPrimaryActionClick
    }

    fun setOnSecondaryActionClick(onSecondaryActionClick: () -> Unit) {
        this.onSecondaryActionClick = onSecondaryActionClick
    }

    fun setOnThirdActionClick(onThirdActionClick: () -> Unit) {
        this.onThirdActionClick = onThirdActionClick
    }
}