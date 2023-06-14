package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.helper.BuyerOrderDetailStickyActionButtonHandler
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton

class BuyerOrderDetailStickyActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var btnBuyerOrderDetailPrimaryActions: UnifyButton? = null
    private var btnBuyerOrderDetailSecondaryActions: UnifyImageButton? = null

    private val primaryActionButtonClickListener: OnClickListener by lazy {
        createPrimaryActionButtonClickListener()
    }
    private val secondaryActionButtonClickListener: OnClickListener by lazy {
        createSecondaryActionButtonClickListener()
    }

    private var viewModel: BuyerOrderDetailViewModel? = null
    private var stickyActionButtonHandler: BuyerOrderDetailStickyActionButtonHandler? = null
    private var bottomSheetManager: BuyerOrderDetailBottomSheetManager? = null

    init {
        View.inflate(context, R.layout.partial_buyer_order_detail_sticky_action_buttons, this).run {
            btnBuyerOrderDetailPrimaryActions = findViewById(R.id.btnBuyerOrderDetailPrimaryActions)
            btnBuyerOrderDetailSecondaryActions = findViewById(R.id.btnBuyerOrderDetailSecondaryActions)
        }
    }

    private fun createPrimaryActionButtonClickListener(): OnClickListener {
        return OnClickListener {
            viewModel?.buyerOrderDetailUiState?.value?.let { uiState ->
                if (uiState is BuyerOrderDetailUiState.HasData) {
                    startPrimaryActionButtonLoading()
                    stickyActionButtonHandler?.onActionButtonClicked(
                        true,
                        uiState.actionButtonsUiState.data.primaryActionButton
                    )
                    if (shouldFinishPrimaryActionButtonImmediately(uiState.actionButtonsUiState.data.primaryActionButton.key)) {
                        finishPrimaryActionButtonLoading()
                    }
                }
            }
        }
    }

    private fun shouldFinishPrimaryActionButtonImmediately(buttonKey: String): Boolean {
        return buttonKey != BuyerOrderDetailActionButtonKey.BUY_AGAIN
    }

    private fun createSecondaryActionButtonClickListener(): OnClickListener {
        return OnClickListener {
            onSecondaryActionButtonClicked()
        }
    }

    private fun onSecondaryActionButtonClicked() {
        bottomSheetManager?.showSecondaryActionButtonBottomSheet(
            viewModel?.getSecondaryActionButtons().orEmpty(), stickyActionButtonHandler
        )
    }

    private fun setupPrimaryButton(
        primaryActionButton: ActionButtonsUiModel.ActionButton
    ) {
        btnBuyerOrderDetailPrimaryActions?.apply {
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
                show()
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    cornerRadius = resources.getDimension(
                        com.tokopedia.unifycomponents.R.dimen.button_corner_radius
                    )
                    setStroke(
                        resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.button_stroke_width),
                        ContextCompat.getColor(
                            context,
                            R.color.buyer_order_detail_dms_secondary_action_button_stroke_color
                        )
                    )
                }
                setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.buyer_order_detail_dms_secondary_action_button_color_filter
                    )
                )
                setOnClickListener(secondaryActionButtonClickListener)
            }
        } else {
            btnBuyerOrderDetailSecondaryActions?.hide()
        }
    }

    private fun startPrimaryActionButtonLoading() {
        btnBuyerOrderDetailPrimaryActions?.isLoading = true
    }

    fun finishPrimaryActionButtonLoading() {
        btnBuyerOrderDetailPrimaryActions?.isLoading = false
    }

    fun setupActionButtons(actionButtonsUiModel: ActionButtonsUiModel) {
        if (actionButtonsUiModel.primaryActionButton.key.isNotBlank()) {
            show()
            setupPrimaryButton(actionButtonsUiModel.primaryActionButton)
            setupSecondaryButton(actionButtonsUiModel.secondaryActionButtons)
        } else {
            hide()
        }
    }

    fun setViewModel(viewModel: BuyerOrderDetailViewModel) {
        this.viewModel = viewModel
    }

    fun setStickyActionButtonClickHandler(handler: BuyerOrderDetailStickyActionButtonHandler) {
        this.stickyActionButtonHandler = handler
    }

    fun setBottomSheetManager(bottomSheetManager: BuyerOrderDetailBottomSheetManager) {
        this.bottomSheetManager = bottomSheetManager
    }
}
