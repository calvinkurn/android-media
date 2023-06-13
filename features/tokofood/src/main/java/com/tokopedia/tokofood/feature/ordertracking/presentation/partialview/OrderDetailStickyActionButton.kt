package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokofood.databinding.TokofoodPartialOrderDetailStickyActionButtonsBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.bottomsheet.SecondaryActionBottomSheet
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.TrackingWrapperUiModel
import com.tokopedia.unifycomponents.BaseCustomView


class OrderDetailStickyActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: TokofoodPartialOrderDetailStickyActionButtonsBinding? = null

    private var navigator: OrderTrackingNavigator? = null

    private var secondaryActionBottomSheet: SecondaryActionBottomSheet? = null

    init {
        binding = TokofoodPartialOrderDetailStickyActionButtonsBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        setBackgroundColor()
    }

    private fun setBackgroundColor() {
        binding?.orderDetailStickyActionButtons?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    private fun onPrimaryActionButtonClickListener(trackingWrapperUiModel: TrackingWrapperUiModel, appUrl: String): OnClickListener {
        return OnClickListener {
            navigator?.goToMerchantPage(trackingWrapperUiModel, appUrl)
        }
    }

    private fun onSecondaryActionButtonClicked(
        trackingWrapperUiModel: TrackingWrapperUiModel,
        secondaryButton: List<ActionButtonsUiModel.ActionButton>,
        fragmentManager: FragmentManager
    ): OnClickListener {
        return OnClickListener {
            if (secondaryActionBottomSheet == null) {
                secondaryActionBottomSheet = SecondaryActionBottomSheet()
            }
            secondaryActionBottomSheet?.run {
                dismissBottomSheet()
                setMerchantData(trackingWrapperUiModel.merchantData)
                setOrderId(trackingWrapperUiModel.orderId)
                setActionBtnList(secondaryButton)
                navigator?.let { navigator -> setNavigator(navigator) }
                show(fragmentManager)
            }
        }
    }

    private fun setupPrimaryButton(
        trackingWrapperUiModel: TrackingWrapperUiModel,
        actionButton: ActionButtonsUiModel.ActionButton
    ) {
        binding?.beliLagiButton?.apply {
            text = actionButton.label
            setOnClickListener(
                onPrimaryActionButtonClickListener(
                    trackingWrapperUiModel,
                    actionButton.appUrl
                )
            )
            show()
        }
    }

    private fun setupSecondaryButton(
        trackingWrapperUiModel: TrackingWrapperUiModel,
        secondaryButton: List<ActionButtonsUiModel.ActionButton>,
        fragmentManager: FragmentManager
    ) {
        binding?.btnOrderDetailSecondaryActions?.run {
            showWithCondition(secondaryButton.isNotEmpty())
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
                        com.tokopedia.tokofood.R.color.food_order_detail_dms_secondary_action_button_stroke_color
                    )
                )
            }
            setColorFilter(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.tokofood.R.color.food_order_detail_dms_secondary_action_button_color_filter
                )
            )
            setOnClickListener(
                onSecondaryActionButtonClicked(
                    trackingWrapperUiModel,
                    secondaryButton,
                    fragmentManager
                )
            )
        }
    }

    fun setupActionButtons(
        trackingWrapperUiModel: TrackingWrapperUiModel,
        actionButtons: ActionButtonsUiModel,
        fragmentManager: FragmentManager
    ) {
        setupPrimaryButton(trackingWrapperUiModel, actionButtons.primaryActionButton)
        setupSecondaryButton(
            trackingWrapperUiModel,
            actionButtons.secondaryActionButton,
            fragmentManager
        )
    }

    fun setOrderTrackingNavigator(navigator: OrderTrackingNavigator) {
        this.navigator = navigator
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }
}
