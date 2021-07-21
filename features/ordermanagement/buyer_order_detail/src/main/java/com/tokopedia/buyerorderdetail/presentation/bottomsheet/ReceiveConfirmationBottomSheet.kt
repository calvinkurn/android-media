package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTrackerConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success

class ReceiveConfirmationBottomSheet(
        context: Context,
        private var actionButton: ActionButtonsUiModel.ActionButton,
        private val bottomSheetManager: BuyerOrderDetailBottomSheetManager,
        private val viewModel: BuyerOrderDetailViewModel,
        private val navigator: BuyerOrderDetailNavigator
) : View.OnClickListener {
    companion object {
        private const val BUTTON_TYPE_BUY = "buy"
        private const val BUTTON_TYPE_PRIMARY = "primary"
        private const val BUTTON_TYPE_SECONDARY = "secondary"
    }

    private val bottomSheet: BottomSheetUnify by lazy {
        setupBottomSheet(context)
    }
    private val childView: View by lazy {
        createChildView(context)
    }

    private var tvDescription: Typography? = null
    private var btnRight: UnifyButton? = null
    private var btnLeft: UnifyButton? = null

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
            bindViews(this)
            setupDescription()
            btnRight?.setupButton(actionButton.popUp.actionButton.lastOrNull())
            btnLeft?.setupButton(actionButton.popUp.actionButton.firstOrNull())
        }
    }

    private fun bindViews(view: View) {
        with(view) {
            tvDescription = findViewById(R.id.tvBottomSheetFinishOrderDescription)
            btnRight = findViewById(R.id.btnFinishOrderRight)
            btnLeft = findViewById(R.id.btnFinishOrderLeft)
        }
    }

    private fun setupDescription() {
        tvDescription?.text = actionButton.popUp.body
    }

    private fun UnifyButton.setupButton(button: ActionButtonsUiModel.ActionButton.PopUp.PopUpButton?) {
        button?.let {
            text = button.displayName
            mapButtonStyle(button.type)
            setOnClickListener(this@ReceiveConfirmationBottomSheet)
        }
    }

    private fun onPrimaryButtonClicked() {
        actionButton.popUp.actionButton.lastOrNull()?.let {
            if (it.key == BuyerOrderDetailActionButtonKey.BACK || it.key.isBlank()) {
                dismiss()
            } else {
                disableDismiss()
                onPopUpActionButtonClicked(it)
            }
        }
    }

    private fun onSecondaryButtonClicked() {
        actionButton.popUp.actionButton.firstOrNull()?.let {
            if (it.key == BuyerOrderDetailActionButtonKey.BACK || it.key.isBlank()) {
                dismiss()
            } else {
                disableDismiss()
                onPopUpActionButtonClicked(it)
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

    private fun onPopUpActionButtonClicked(button: ActionButtonsUiModel.ActionButton.PopUp.PopUpButton) {
        val buttonName = when (button.key) {
            BuyerOrderDetailActionButtonKey.FINISH_ORDER -> {
                onDoReceiveConfirmationActionButtonClicked()
                BuyerOrderDetailTrackerConstant.BUTTON_NAME_FINISH_ORDER_CONFIRMATION_CONFIRM_FINISH_ORDER
            }
            BuyerOrderDetailActionButtonKey.REQUEST_COMPLAINT -> {
                onComplaintActionButtonClicked(button.uri)
                BuyerOrderDetailTrackerConstant.BUTTON_NAME_FINISH_ORDER_CONFIRMATION_REQUEST_COMPLAINT
            }
            else -> ""
        }
        if (buttonName.isNotBlank()) {
            trackClickActionButtonFromReceiveConfirmation(buttonName)
        }
    }

    private fun onDoReceiveConfirmationActionButtonClicked() {
        viewModel.finishOrder()
    }

    private fun onComplaintActionButtonClicked(complaintUrl: String) {
        navigator.goToCreateResolution(complaintUrl)
        bottomSheetManager.finishReceiveConfirmationBottomSheetLoading()
    }

    private fun trackClickActionButtonFromReceiveConfirmation(buttonName: String) {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                BuyerOrderDetailTracker.eventClickActionButtonFromReceiveConfirmation(
                        buttonName = buttonName,
                        orderId = it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId,
                        orderStatusCode = it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId)
            }
        }
    }

    private fun UnifyButton.mapButtonStyle(typeString: String) {
        when (typeString) {
            BUTTON_TYPE_BUY,BUTTON_TYPE_PRIMARY -> {
                buttonType = UnifyButton.Type.MAIN
                buttonVariant = UnifyButton.Variant.FILLED
            }
            BUTTON_TYPE_SECONDARY -> {
                buttonType = UnifyButton.Type.ALTERNATE
                buttonVariant = UnifyButton.Variant.GHOST
            }
            else -> {
                buttonType = UnifyButton.Type.MAIN
                buttonVariant = UnifyButton.Variant.FILLED
            }
        }
    }

    override fun onClick(v: View?) {
        if (v is UnifyButton) {
            v.isLoading = true
        }
        when (v?.id) {
            R.id.btnFinishOrderLeft -> onSecondaryButtonClicked()
            R.id.btnFinishOrderRight -> onPrimaryButtonClicked()
        }
    }

    fun reInit(actionButton: ActionButtonsUiModel.ActionButton) {
        this.actionButton = actionButton
        setupDescription()
        btnRight?.apply {
            isLoading = false
            setupButton(actionButton.popUp.actionButton.lastOrNull())
        }
        btnLeft?.apply {
            isLoading = false
            setupButton(actionButton.popUp.actionButton.firstOrNull())
        }
        enableDismiss()
    }

    fun show(fragmentManager: FragmentManager) {
        bottomSheet.show(fragmentManager, SecondaryActionButtonBottomSheet::class.java.simpleName)
    }

    fun dismiss() {
        if (bottomSheet.isAdded) {
            bottomSheet.dismiss()
        }
    }

    fun finishLoading() {
        btnRight?.isLoading = false
        btnLeft?.isLoading = false
        enableDismiss()
    }
}