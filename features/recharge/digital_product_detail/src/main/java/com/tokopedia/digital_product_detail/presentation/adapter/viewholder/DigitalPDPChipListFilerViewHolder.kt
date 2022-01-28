package com.tokopedia.digital_product_detail.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterChipListBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalChipsAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DigitalPDPChipListFilerViewHolder (private val binding: ViewPdpFilterChipListBinding
): RecyclerView.ViewHolder(binding.root){

    fun bind(element: TelcoFilterTagComponent){
        val chipsAdapter = DigitalChipsAdapter()
        with(binding){
            chipsAdapter.setChipList(element.filterTagDataCollections)
            tgTitleChipsList.text = element.text

            if (element.filterTagDataCollections.size > MAX_FILTER_SIZE) tgSeeAllChipsList.show()
            else tgSeeAllChipsList.hide()

            rvChipsList.run {
                adapter = chipsAdapter
                layoutManager = ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            }
        }
    }

    companion object {
        const val MAX_FILTER_SIZE = 5
    }
}