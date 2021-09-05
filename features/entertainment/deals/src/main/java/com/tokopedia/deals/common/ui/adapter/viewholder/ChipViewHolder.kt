package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsChipListener
import com.tokopedia.deals.common.ui.dataview.ChipDataView
import com.tokopedia.deals.databinding.ItemDealsChipNormalBinding
import com.tokopedia.unifycomponents.ChipsUnify

class ChipViewHolder(itemView: View, private val dealsChipListener: DealsChipListener) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(chip: ChipDataView) {
        val binding = ItemDealsChipNormalBinding.bind(itemView)
        binding.run {
            chipItem.chipText = chip.title
            chipItem.chipType = if (chip.isSelected) ChipsUnify.TYPE_SELECTED
            else ChipsUnify.TYPE_NORMAL

            this.root.setOnClickListener {
                dealsChipListener.onChipClicked(it, chip, adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_chip_normal
    }
}