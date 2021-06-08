package com.tokopedia.buyerorderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.unifyprinciples.Typography

class SecondaryActionButtonAdapter(private val listener: ActionButtonClickListener) : RecyclerView.Adapter<SecondaryActionButtonAdapter.ViewHolder>() {

    private var secondaryActionButtons: List<ActionButtonsUiModel.ActionButton> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_buyer_order_detail_secondary_action_button, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return secondaryActionButtons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(secondaryActionButtons.getOrNull(position))
    }

    fun setSecondaryActionButtons(secondaryActionButtons: List<ActionButtonsUiModel.ActionButton>) {
        this.secondaryActionButtons = secondaryActionButtons
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val listener: ActionButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        private val tvBuyerOrderDetailSecondaryActionButton = itemView.findViewById<Typography>(R.id.tvBuyerOrderDetailSecondaryActionButton)

        fun bind(button: ActionButtonsUiModel.ActionButton?) {
            tvBuyerOrderDetailSecondaryActionButton.text = button?.label.orEmpty()
            tvBuyerOrderDetailSecondaryActionButton.setOnClickListener {
                button?.let {
                    listener.onActionButtonClicked(false, button)
                }
            }
        }
    }
}