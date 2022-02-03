package com.tokopedia.digital_product_detail.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterChipListBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalChipsAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DigitalPDPChipListFilterViewHolder (private val binding: ViewPdpFilterChipListBinding,
                                          private val chipListener: DigitalPDPChipFilterViewHolder.ChipListener,
                                          private val listListener: ListFilterListener): RecyclerView.ViewHolder(binding.root){

    fun bind(element: TelcoFilterTagComponent){
        val chipsAdapter = DigitalChipsAdapter(chipListener, MAX_FILTER_SIZE)
        with(binding){
            chipsAdapter.setChipList(element)
            tgTitleChipsList.text = element.text

            if (element.filterTagDataCollections.size > MAX_FILTER_SIZE) tgSeeAllChipsList.show()
            else tgSeeAllChipsList.hide()

            tgSeeAllChipsList.setOnClickListener {
                listListener.onSeeAllClicked(element, position)
            }

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

    interface ListFilterListener{
        fun onSeeAllClicked(element: TelcoFilterTagComponent, position: Int)
    }
}