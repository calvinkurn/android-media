package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_action_buttons.view.*

class ActionButtonsViewHolder(itemView: View?, private val listener: ActionButtonClickListener) : AbstractViewHolder<ActionButtonsUiModel>(itemView), View.OnClickListener {
    companion object {
        const val SECONDARY_ACTION_BUTTON_KEY = "secondary_action"

        val LAYOUT = R.layout.item_buyer_order_detail_action_buttons
    }

    private var element: ActionButtonsUiModel? = null

    init {
        itemView?.btnBuyerOrderDetailSecondaryActions?.setOnClickListener(this)
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

    private fun onPrimaryActionButtonClicked() {
        element?.let {
            listener.onActionButtonClicked(it.primaryActionButton.key)
        }
    }

    private fun onSecondaryActionButtonClicked() {
        listener.onActionButtonClicked(SECONDARY_ACTION_BUTTON_KEY)
    }
}