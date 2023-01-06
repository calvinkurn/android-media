package com.tokopedia.topads.debit.autotopup.view.adapter.viewholder

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.debit.autotopup.data.model.TopUpCreditItemData
import com.tokopedia.unifyprinciples.Typography

class CreditItemViewHolder(itemView: View, private val onClicked: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val creditItemText: Typography = itemView.findViewById(R.id.creditItemText)
    private val creditItemBonus: Typography = itemView.findViewById(R.id.creditItemBonus)
    private val creditItem: View = itemView.findViewById(R.id.creditItem)
    fun bind(list: ArrayList<TopUpCreditItemData>, position: Int) {
        creditItemText.text = list[position].productPrice
        creditItemBonus.text = list[position].bonus
        changeCreditItemState(list[position].clicked)
        creditItem.setOnClickListener {
            onClicked(position)
        }
    }

    private fun changeCreditItemState(clicked: Boolean) {
        if (clicked) {
            creditItem.background = AppCompatResources.getDrawable(
                itemView.context,
                R.drawable.top_ads_selected_credit_item_bg
            )
            creditItemBonus.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
            creditItemBonus.weightType = Typography.BOLD
        } else {
            creditItem.background = AppCompatResources.getDrawable(
                itemView.context,
                R.drawable.top_ads_credit_item_bg
            )
            creditItemBonus.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            )
            creditItemBonus.weightType = Typography.REGULAR
        }
    }
}

