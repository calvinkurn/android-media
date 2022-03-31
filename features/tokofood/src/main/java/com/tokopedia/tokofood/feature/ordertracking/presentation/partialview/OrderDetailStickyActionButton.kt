package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.tokofood.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton


class OrderDetailStickyActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var btnOrderDetailPrimaryActions: UnifyButton? = null
    private var btnOrderDetailSecondaryActions: UnifyImageButton? = null

    private val primaryActionButtonClickListener: OnClickListener by lazy {
        createPrimaryActionButtonClickListener()
    }
    private val secondaryActionButtonClickListener: OnClickListener by lazy {
        createSecondaryActionButtonClickListener()
    }

    init {
        View.inflate(context, R.layout.tokofood_partial_order_detail_sticky_action_buttons, this).run {
            btnOrderDetailPrimaryActions = findViewById(R.id.btnOrderDetailPrimaryActions)
            btnOrderDetailSecondaryActions = findViewById(R.id.btnOrderDetailSecondaryActions)
        }
        setupSecondaryButton()
    }

    private fun createPrimaryActionButtonClickListener(): OnClickListener {
        return OnClickListener {

        }
    }

    private fun createSecondaryActionButtonClickListener(): OnClickListener {
        return OnClickListener {
            onSecondaryActionButtonClicked()
        }
    }

    private fun onSecondaryActionButtonClicked() {

    }

    private fun setupPrimaryButton(

    ) {

    }

    private fun setupSecondaryButton() {
        btnOrderDetailSecondaryActions?.apply {
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
            setOnClickListener(secondaryActionButtonClickListener)
        }
    }

    private fun startPrimaryActionButtonLoading() {

    }

    fun finishPrimaryActionButtonLoading() {

    }

    fun setupActionButtons() {

    }

    fun setStickyActionButtonClickHandler() {

    }
}