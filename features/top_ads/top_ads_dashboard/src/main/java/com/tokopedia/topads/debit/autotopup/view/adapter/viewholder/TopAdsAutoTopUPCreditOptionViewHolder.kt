package com.tokopedia.topads.debit.autotopup.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.topads_dash_credit_option_chip.view.*

class TopAdsAutoTopUPCreditOptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val chip: ChipsUnify = itemView.creditOptionChipItem

    fun bind(data1: AutoTopUpItem) {
        chip.chipText = data1.priceFmt
    }

    fun toggleActivate(isActive: Boolean) {
        if (isActive) {
            chip.chipType = ChipsUnify.TYPE_SELECTED

        } else {
            chip.chipType = ChipsUnify.TYPE_NORMAL

        }
    }
}
