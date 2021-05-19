package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ReceiveConfirmationBottomSheet(
        context: Context,
        private var actionButton: ActionButtonsUiModel.ActionButton,
        private val actionButtonClickListener: ActionButtonClickListener
) : View.OnClickListener {
    private val bottomSheet: BottomSheetUnify by lazy {
        setupBottomSheet(context)
    }
    private val childView: View by lazy {
        createChildView(context)
    }

    private fun View.getDescriptionView() = findViewById<Typography>(R.id.tvBottomSheetFinishOrderDescription)
    private fun View.getPrimaryButtonView() = findViewById<UnifyButton>(R.id.btnPrimary)
    private fun View.getSecondaryButtonView() = findViewById<UnifyButton>(R.id.btnSecondary)

    private fun setupBottomSheet(context: Context): BottomSheetUnify {
        return BottomSheetUnify().apply {
            setTitle(context.getString(R.string.receive_confirmation_bottomsheet_header))
            showCloseIcon = true
            overlayClickDismiss = true
            setChild(childView)
        }
    }

    private fun createChildView(context: Context): View {
        return View.inflate(context, R.layout.bottomsheet_finish_order, null).apply {
            getDescriptionView().setupDescription()
            getPrimaryButtonView().setupButton(actionButton.popUp.actionButton.firstOrNull())
            getSecondaryButtonView().setupButton(actionButton.popUp.actionButton.lastOrNull())
        }
    }

    private fun Typography.setupDescription() {
        text = actionButton.popUp.body
    }

    private fun UnifyButton.setupButton(button: ActionButtonsUiModel.ActionButton.PopUp.PopUpButton?) {
        button?.let {
            text = button.displayName
            buttonType = Utils.mapButtonVariant(button.type)
            setOnClickListener(this@ReceiveConfirmationBottomSheet)
        }
    }

    private fun onPrimaryButtonClicked() {
        actionButton.popUp.actionButton.firstOrNull()?.let {
            if (it.key == BuyerOrderDetailConst.ACTION_BUTTON_KEY_BACK) {
                dismiss()
            } else {
                disableDismiss()
                actionButtonClickListener.onPopUpActionButtonClicked(it)
            }
        }
    }

    private fun onSecondaryButtonClicked() {
        actionButton.popUp.actionButton.lastOrNull()?.let {
            if (it.key == BuyerOrderDetailConst.ACTION_BUTTON_KEY_BACK) {
                dismiss()
            } else {
                disableDismiss()
                actionButtonClickListener.onPopUpActionButtonClicked(it)
            }
        }
    }

    private fun disableDismiss() {
        bottomSheet.overlayClickDismiss = false
        bottomSheet.isCancelable = false
        bottomSheet.showCloseIcon = false
    }

    private fun enableDismiss() {
        bottomSheet.overlayClickDismiss = true
        bottomSheet.isCancelable = true
        bottomSheet.showCloseIcon = true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPrimary -> onPrimaryButtonClicked()
            R.id.btnSecondary -> onSecondaryButtonClicked()
        }
        if (v is UnifyButton) {
            v.isLoading = true
        }
    }

    fun reInit(actionButton: ActionButtonsUiModel.ActionButton) {
        this.actionButton = actionButton
        childView.getDescriptionView().setupDescription()
        childView.getPrimaryButtonView().apply {
            isLoading = false
            setupButton(actionButton.popUp.actionButton.firstOrNull())
        }
        childView.getSecondaryButtonView().apply {
            isLoading = false
            setupButton(actionButton.popUp.actionButton.lastOrNull())
        }
        enableDismiss()
    }

    fun show(fragmentManager: FragmentManager) {
        bottomSheet.show(fragmentManager, SecondaryActionButtonBottomSheet::class.java.simpleName)
    }

    fun dismiss() {
        bottomSheet.dismiss()
    }

    fun finishLoading() {
        childView.getPrimaryButtonView().isLoading = false
        childView.getSecondaryButtonView().isLoading = false
        enableDismiss()
    }
}