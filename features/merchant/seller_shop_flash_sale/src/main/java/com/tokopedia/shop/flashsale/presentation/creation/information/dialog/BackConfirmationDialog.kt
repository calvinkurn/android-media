package com.tokopedia.shop.flashsale.presentation.creation.information.dialog

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.UnifyButton

@SuppressLint("UnifyComponentUsage")
class BackConfirmationDialog(private val context: Context) {

    private var btnPrimaryAction: UnifyButton? = null
    private var btnSecondaryAction: UnifyButton? = null
    private var btnThirdAction: UnifyButton? = null
    private var onPrimaryActionClick: () -> Unit = {}
    private var onSecondaryActionClick: () -> Unit = {}
    private var onThirdActionClick: () -> Unit = {}

    private val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)

    init {
        setupMargin()
        val view = View.inflate(context, R.layout.ssfs_dialog_back_confirmation, null)
        dialog.setUnlockVersion()
        dialog.setChild(view)

        btnPrimaryAction = view.findViewById(R.id.btnYes)
        btnSecondaryAction = view.findViewById(R.id.btnNo)
        btnThirdAction = view.findViewById(R.id.btnSaveAsDraft)
        setupView()
    }

    private fun setupMargin() {

            val layoutParams = dialog.dialogContent.layoutParams as ConstraintLayout.LayoutParams

            layoutParams.setMargins(layoutParams.marginStart, 0,
                layoutParams.marginEnd, 0)
            dialog.dialogContent.layoutParams = layoutParams


    }


    fun show() {
     /*   val layoutParams = dialog.dialogContainer.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(layoutParams.leftMargin, 0, layoutParams.rightMargin, 0)
        dialog.dialogContainer.layoutParams = layoutParams*/
        //dialog.dialogContent.layoutParams = layoutParams
        //dialog.dialogImageContainer.layoutParams = layoutParams
       // dialog.dialogCTAContainer.layoutParams = layoutParams
        dialog.show()
    }

    private fun setupView() {
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


   /* fun show(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val view = View.inflate(context, R.layout.ssfs_dialog_back_confirmation, null)
        dialog.setContentView(view)

        setupView(dialog)

        dialog.show()
    }*/

    private fun setupView(dialog: Dialog) {
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