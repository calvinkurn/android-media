package com.tokopedia.buyerorderdetail.presentation.partialview

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.helper.BuyerOrderDetailStickyActionButtonHandler
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.usecase.coroutines.Success

class BuyerOrderDetailStickyActionButton(
        private val bottomSheetManager: BuyerOrderDetailBottomSheetManager,
        private val stickyActionButtonHandler: BuyerOrderDetailStickyActionButtonHandler,
        private val viewModel: BuyerOrderDetailViewModel,
        fragmentView: View?
) {
    private val containerBuyerOrderDetail: ConstraintLayout? = fragmentView?.findViewById(R.id.containerBuyerOrderDetail)
    private val containerActionButtons: CardView? = fragmentView?.findViewById(R.id.containerActionButtons)
    private val btnBuyerOrderDetailPrimaryActions: UnifyButton? = fragmentView?.findViewById(R.id.btnBuyerOrderDetailPrimaryActions)
    private val btnBuyerOrderDetailSecondaryActions: UnifyImageButton? = fragmentView?.findViewById(R.id.btnBuyerOrderDetailSecondaryActions)

    private val primaryActionButtonClickListener: View.OnClickListener by lazy {
        createPrimaryActionButtonClickListener()
    }
    private val secondaryActionButtonClickListener: View.OnClickListener by lazy {
        createSecondaryActionButtonClickListener()
    }

    private fun createPrimaryActionButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.buyerOrderDetailResult.value?.let {
                if (it is Success) {
                    startPrimaryActionButtonLoading()
                    stickyActionButtonHandler.onActionButtonClicked(true, it.data.actionButtonsUiModel.primaryActionButton)
                    if (shouldFinishPrimaryActionButtonImmediately(it.data.actionButtonsUiModel.primaryActionButton.key)) {
                        finishPrimaryActionButtonLoading()
                    }
                }
            }
        }
    }

    private fun shouldFinishPrimaryActionButtonImmediately(buttonKey: String): Boolean {
        return buttonKey != BuyerOrderDetailActionButtonKey.BUY_AGAIN
    }

    private fun createSecondaryActionButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            onSecondaryActionButtonClicked()
        }
    }

    private fun onSecondaryActionButtonClicked() {
        bottomSheetManager.showSecondaryActionButtonBottomSheet(viewModel.getSecondaryActionButtons(), stickyActionButtonHandler)
    }

    private fun setupPrimaryButton(primaryActionButton: ActionButtonsUiModel.ActionButton, secondaryActionButtonCount: Int) {
        btnBuyerOrderDetailPrimaryActions?.apply {
            val layoutParamsCopy = layoutParams as ViewGroup.MarginLayoutParams
            layoutParamsCopy.marginStart = if (secondaryActionButtonCount == 0) 0 else getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            layoutParams = layoutParamsCopy
            text = primaryActionButton.label
            buttonVariant = Utils.mapButtonVariant(primaryActionButton.variant)
            buttonType = Utils.mapButtonType(primaryActionButton.type)
            setOnClickListener(primaryActionButtonClickListener)
            show()
        }
    }

    private fun setupSecondaryButton(secondaryActionButtons: List<ActionButtonsUiModel.ActionButton>) {
        if (secondaryActionButtons.isNotEmpty()) {
            btnBuyerOrderDetailSecondaryActions?.apply {
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    cornerRadius = resources.getDimension(com.tokopedia.unifycomponents.R.dimen.button_corner_radius)
                    setStroke(resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.button_stroke_width), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color))
                }
                setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color))
                setOnClickListener(secondaryActionButtonClickListener)
                show()
            }
        } else {
            btnBuyerOrderDetailSecondaryActions?.gone()
        }
    }

    fun setupActionButtons(actionButtonsUiModel: ActionButtonsUiModel) {
        if (actionButtonsUiModel.primaryActionButton.key.isNotBlank()) {
            setupPrimaryButton(actionButtonsUiModel.primaryActionButton, actionButtonsUiModel.secondaryActionButtons.size)
            setupSecondaryButton(actionButtonsUiModel.secondaryActionButtons)
        }
    }

    private fun startPrimaryActionButtonLoading() {
        btnBuyerOrderDetailPrimaryActions?.isLoading = true
    }

    fun finishPrimaryActionButtonLoading() {
        btnBuyerOrderDetailPrimaryActions?.isLoading = false
    }
}