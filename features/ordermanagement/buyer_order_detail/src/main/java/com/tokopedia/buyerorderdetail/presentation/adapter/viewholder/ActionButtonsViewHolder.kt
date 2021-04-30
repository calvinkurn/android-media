package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_action_buttons.view.*

class ActionButtonsViewHolder(itemView: View?, private val listener: ActionButtonClickListener) : AbstractViewHolder<ActionButtonsUiModel>(itemView), View.OnClickListener {
    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_action_buttons
    }

    private var element: ActionButtonsUiModel? = null

    init {
        setupSecondaryButton()
    }

    override fun bind(element: ActionButtonsUiModel?) {
        this.element = element
        setupPrimaryButton()
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.btnBuyerOrderDetailPrimaryActions -> onPrimaryActionButtonClicked()
            R.id.btnBuyerOrderDetailSecondaryActions -> onSecondaryActionButtonClicked()
        }
    }

    private fun setupPrimaryButton() {
        with(itemView) {
            btnBuyerOrderDetailPrimaryActions.text = element?.primaryActionButton?.label.orEmpty()
            btnBuyerOrderDetailPrimaryActions.setOnClickListener(this@ActionButtonsViewHolder)
        }
    }

    private fun setupSecondaryButton() {
        itemView.btnBuyerOrderDetailSecondaryActions?.apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(ContextCompat.getColor(context, android.R.color.transparent))
                cornerRadius = resources.getDimension(com.tokopedia.unifycomponents.R.dimen.button_corner_radius)
                setStroke(resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.button_stroke_width), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color))
            }
            setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N200))
            setOnClickListener(this@ActionButtonsViewHolder)
        }
    }

    private fun onPrimaryActionButtonClicked() {
        element?.let {
            listener.onActionButtonClicked(it.primaryActionButton)
        }
    }

    private fun onSecondaryActionButtonClicked() {
        listener.onSecondaryActionButtonClicked()
    }
}