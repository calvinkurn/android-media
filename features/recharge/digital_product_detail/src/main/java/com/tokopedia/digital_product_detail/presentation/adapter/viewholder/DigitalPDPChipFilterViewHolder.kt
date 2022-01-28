package com.tokopedia.digital_product_detail.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterChipBinding
import com.tokopedia.unifycomponents.ChipsUnify

class DigitalPDPChipFilterViewHolder(private val binding: ViewPdpFilterChipBinding,
                                     private val chipListener: ChipListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(element: FilterTagDataCollection){
        with(binding){
            chipPdpItem.run {
                chipText = element.value
                chipType = if (element.isSelected) ChipsUnify.TYPE_SELECTED
                else ChipsUnify.TYPE_NORMAL

                root.setOnClickListener {
                    chipListener.onChipClicked(element, position)
                }
            }
        }
    }

    interface ChipListener {
        fun onChipClicked(element: FilterTagDataCollection, position: Int)
    }
}