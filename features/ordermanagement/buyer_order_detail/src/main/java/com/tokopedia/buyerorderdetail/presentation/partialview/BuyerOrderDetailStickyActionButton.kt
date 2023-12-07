package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.helper.BuyerOrderDetailStickyActionButtonHandler
import com.tokopedia.buyerorderdetail.presentation.model.SavingsWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.DarkModeUtil

class BuyerOrderDetailStickyActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var btnBuyerOrderDetailPrimaryActions: UnifyButton? = null
    private var btnBuyerOrderDetailSecondaryActions: UnifyImageButton? = null
    private var savingWidgetButton: LinearLayoutCompat? = null

    private var leftTextSavingWidget: Typography? = null
    private var rightTextSavingWidget: Typography? = null
    private var imgCenterSavingWidget: ImageUnify? = null

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
            savingWidgetButton = findViewById(R.id.savingWidgetBuyerOrderDetail)
            leftTextSavingWidget = findViewById(R.id.tvLeftSavingWidget)
            rightTextSavingWidget= findViewById(R.id.tvRightSavingWidget)
            imgCenterSavingWidget= findViewById(R.id.imgMiddleSavingWidget)
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

    fun setupSavingWidget(savingsWidgetUiModel: SavingsWidgetUiModel) {
        savingWidgetButton?.show()
        val tickerData = savingsWidgetUiModel.plusTicker
        leftTextSavingWidget?.showIfWithBlock(tickerData.leftText.isNotEmpty()) {
            text = HtmlLinkHelper(
                context, DarkModeUtil.getHtmlTextDarkModeSupport(
                    context,
                    tickerData.leftText
                )
            ).spannedString
        }

        rightTextSavingWidget?.showIfWithBlock(tickerData.rightText.isNotEmpty()) {
            text = HtmlLinkHelper(context, DarkModeUtil.getHtmlTextDarkModeSupport(
                context,
                tickerData.rightText
            )).spannedString
        }

        imgCenterSavingWidget?.showIfWithBlock(tickerData.imageUrl.isNotEmpty()) {
            loadImage(
                tickerData.imageUrl
            )
        }

        savingWidgetButton?.setOnClickListener {
            stickyActionButtonHandler?.onSavingsWidgetClicked(
                plusComponent = savingsWidgetUiModel.plusComponents,
                isPlus = savingsWidgetUiModel.isPlus,
                isMixPromo = tickerData.rightText.isNotEmpty()
            )
        }
    }

    fun hideSavingWidget() {
        savingWidgetButton?.hide()
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
