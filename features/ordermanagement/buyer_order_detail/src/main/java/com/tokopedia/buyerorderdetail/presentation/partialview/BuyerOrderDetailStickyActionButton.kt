package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.helper.BuyerOrderDetailStickyActionButtonHandler
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.usecase.coroutines.Success

class BuyerOrderDetailStickyActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {
    private var motionLayout: MotionLayout? = null
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
            motionLayout = findViewById(R.id.buyerOrderDetailStickyActionButtons)
        }
        setupSecondaryButton()
    }

    private fun createPrimaryActionButtonClickListener(): OnClickListener {
        return OnClickListener {
            viewModel?.buyerOrderDetailResult?.value?.let {
                if (it is Success) {
                    startPrimaryActionButtonLoading()
                    stickyActionButtonHandler?.onActionButtonClicked(
                        true,
                        it.data.actionButtonsUiModel.primaryActionButton
                    )
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

    private fun setupSecondaryButton() {
        btnBuyerOrderDetailSecondaryActions?.apply {
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
                        com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color
                    )
                )
            }
            setColorFilter(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color
                )
            )
            setOnClickListener(secondaryActionButtonClickListener)
        }
    }

    private fun animateChanges(animateChanges: Boolean, shouldShowSecondaryButton: Boolean) {
        if (shouldShowSecondaryButton) {
            animateShowAllButtons(animateChanges)
        } else {
            animateShowPrimaryButtonOnly(animateChanges)
        }
    }

    private fun animateShowAllButtons(animateChanges: Boolean) {
        if (motionLayout?.currentState.orZero() == R.id.show_only_primary_buttons) {
            motionLayout?.setTransition(R.id.show_only_primary_buttons, R.id.show_all_buttons)
            if (animateChanges) {
                motionLayout?.transitionToEnd()
            } else {
                motionLayout?.progress = 1f
            }
        }
    }

    private fun animateShowPrimaryButtonOnly(animateChanges: Boolean) {
        if (motionLayout?.currentState.orZero() == R.id.show_all_buttons) {
            motionLayout?.setTransition(R.id.show_all_buttons, R.id.show_only_primary_buttons)
            if (animateChanges) {
                motionLayout?.transitionToEnd()
            } else {
                motionLayout?.progress = 1f
            }
        }
    }

    private fun startPrimaryActionButtonLoading() {
        btnBuyerOrderDetailPrimaryActions?.isLoading = true
    }

    fun finishPrimaryActionButtonLoading() {
        btnBuyerOrderDetailPrimaryActions?.isLoading = false
    }

    fun setupActionButtons(actionButtonsUiModel: ActionButtonsUiModel, animateChanges: Boolean) {
        if (actionButtonsUiModel.primaryActionButton.key.isNotBlank()) {
            setupPrimaryButton(actionButtonsUiModel.primaryActionButton)
            animateChanges(animateChanges, actionButtonsUiModel.secondaryActionButtons.isNotEmpty())
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