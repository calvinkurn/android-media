package com.tokopedia.topads.debit.autotopup.view.adapter.viewholder

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.debit.autotopup.data.model.TopUpCreditItemData
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsCreditListAdapter
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography

class CreditItemViewHolder(
    itemView: View,
    private val nominalClickListener: TopAdsCreditListAdapter.NominalClickListener?
) :
    RecyclerView.ViewHolder(itemView) {
    private val creditItemText: Typography = itemView.findViewById(R.id.creditItemText)
    private val creditItemBonus: Typography = itemView.findViewById(R.id.creditItemBonus)
    private val nominalShimmer: LoaderUnify = itemView.findViewById(R.id.nominalShimmer)
    private val creditItem: ConstraintLayout = itemView.findViewById(R.id.creditOptionChipItem)
    fun bind(list: ArrayList<TopUpCreditItemData>, position: Int) {
        if (list[position].productPrice.isEmpty()){
            nominalShimmer.show()
        }else{
            nominalShimmer.hide()
            creditItemText.text = list[position].productPrice

            creditItemBonus.showWithCondition(list[position].bonus != "Bonus Rp0")
            creditItemBonus.text = list[position].bonus
            changeCreditItemState(list[position].clicked)
            creditItem.setOnClickListener {
                nominalClickListener?.onNominalClicked(list, position)
            }
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

