package com.tokopedia.topads.debit.autotopup.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.topads_dash_credit_option_chip.view.*

class TopAdsAutoTopUPCreditOptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val chip: ChipsUnify = itemView.creditOptionChipItem

    fun bind(data1: DataCredit) {
        chip.chipText = data1.productPrice
    }

    fun toggleActivate(isActive: Boolean) {
        if (isActive) {
            chip.chipType = ChipsUnify.TYPE_SELECTED

        } else {
            chip.chipType = ChipsUnify.TYPE_NORMAL

        }
    }
}
