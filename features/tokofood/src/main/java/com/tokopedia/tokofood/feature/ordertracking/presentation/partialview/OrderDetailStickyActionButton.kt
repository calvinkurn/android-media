package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.TokofoodPartialOrderDetailStickyActionButtonsBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.bottomsheet.SecondaryActionBottomSheet
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
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
    }

    private fun onPrimaryActionButtonClickListener(appUrl: String): OnClickListener {
        return OnClickListener {
            navigator?.goToMerchantPage(appUrl)
        }
    }

    private fun onSecondaryActionButtonClicked(
        secondaryButton: List<ActionButtonsUiModel.ActionButton>,
        fragmentManager: FragmentManager
    ): OnClickListener {
        return OnClickListener {
            if (secondaryActionBottomSheet == null) {
                secondaryActionBottomSheet = SecondaryActionBottomSheet()
            }
            secondaryActionBottomSheet?.run {
                setActionBtnList(secondaryButton)
                dismissBottomSheet()
                show(fragmentManager)
            }
        }
    }

    private fun setupPrimaryButton(
        actionButton: ActionButtonsUiModel.ActionButton
    ) {
        binding?.btnOrderDetailPrimaryActions?.apply {
            text = actionButton.label
            setOnClickListener(onPrimaryActionButtonClickListener(actionButton.appUrl))
            show()
        }
    }

    private fun setupSecondaryButton(
        secondaryButton: List<ActionButtonsUiModel.ActionButton>,
        fragmentManager: FragmentManager
    ) {
        binding?.btnOrderDetailSecondaryActions?.run {
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
                        R.color.food_order_detail_dms_secondary_action_button_stroke_color
                    )
                )
            }
            setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.food_order_detail_dms_secondary_action_button_color_filter
                )
            )
            setOnClickListener(onSecondaryActionButtonClicked(secondaryButton, fragmentManager))
        }
    }

    fun setupActionButtons(actionButtons: ActionButtonsUiModel, fragmentManager: FragmentManager) {
        setupPrimaryButton(actionButtons.primaryActionButton)
        setupSecondaryButton(actionButtons.secondaryActionButton, fragmentManager)
    }

    fun setOrderTrackingNavigator(navigator: OrderTrackingNavigator) {
        this.navigator = navigator
    }
}