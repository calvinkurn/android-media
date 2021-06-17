package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel

class BuyerOrderDetailBottomSheetManager(
        private val context: Context,
        private val fragmentManager: FragmentManager
) {
    private var bottomSheetReceiveConfirmation: ReceiveConfirmationBottomSheet? = null
    private var secondaryActionButtonBottomSheet: SecondaryActionButtonBottomSheet? = null

    private fun createReceiveConfirmationBottomSheet(
            button: ActionButtonsUiModel.ActionButton,
            bottomSheetManager: BuyerOrderDetailBottomSheetManager,
            navigator: BuyerOrderDetailNavigator,
            viewModel: BuyerOrderDetailViewModel
    ): ReceiveConfirmationBottomSheet {
        return ReceiveConfirmationBottomSheet(context, button, bottomSheetManager, viewModel, navigator)
    }

    private fun createSecondaryActionButtonBottomSheet(actionButtonClickListener: ActionButtonClickListener): SecondaryActionButtonBottomSheet {
        return SecondaryActionButtonBottomSheet(context, actionButtonClickListener)
    }

    fun showReceiveConfirmationBottomSheet(
            button: ActionButtonsUiModel.ActionButton,
            bottomSheetManager: BuyerOrderDetailBottomSheetManager,
            navigator: BuyerOrderDetailNavigator,
            viewModel: BuyerOrderDetailViewModel
    ) {
        val bottomSheetReceiveConfirmation = bottomSheetReceiveConfirmation?.apply {
            reInit(button)
        } ?: createReceiveConfirmationBottomSheet(button, bottomSheetManager, navigator, viewModel)
        this.bottomSheetReceiveConfirmation = bottomSheetReceiveConfirmation
        bottomSheetReceiveConfirmation.show(fragmentManager)
    }

    fun showSecondaryActionButtonBottomSheet(secondaryActionButtons: List<ActionButtonsUiModel.ActionButton>, actionButtonClickListener: ActionButtonClickListener) {
        val secondaryActionButtonBottomSheet = secondaryActionButtonBottomSheet
                ?: createSecondaryActionButtonBottomSheet(actionButtonClickListener)
        this.secondaryActionButtonBottomSheet = secondaryActionButtonBottomSheet
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(fragmentManager)
    }

    fun dismissBottomSheets() {
        secondaryActionButtonBottomSheet?.dismiss()
        bottomSheetReceiveConfirmation?.dismiss()
    }

    fun finishReceiveConfirmationBottomSheetLoading() {
        bottomSheetReceiveConfirmation?.finishLoading()
    }
}