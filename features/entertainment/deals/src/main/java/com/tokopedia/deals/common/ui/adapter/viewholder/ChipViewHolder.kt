package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsChipListener
import com.tokopedia.deals.common.ui.dataview.ChipDataView
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_deals_chip_normal.view.*

class ChipViewHolder(itemView: View, private val dealsChipListener: DealsChipListener) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(chip: ChipDataView) {
        itemView.run {
            chip_item.chipText = chip.title
            chip_item.chipType = if (chip.isSelected) ChipsUnify.TYPE_SELECTED
            else ChipsUnify.TYPE_NORMAL

            setOnClickListener {
                dealsChipListener.onChipClicked(this, chip, adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_chip_normal
    }
}