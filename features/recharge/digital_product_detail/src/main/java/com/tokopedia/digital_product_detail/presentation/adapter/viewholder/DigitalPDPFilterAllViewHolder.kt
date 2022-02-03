package com.tokopedia.digital_product_detail.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterCheckboxBinding

class DigitalPDPFilterAllViewHolder(
    private val binding: ViewPdpFilterCheckboxBinding,
    private val checkBoxListener: CheckBoxListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tagComponent: TelcoFilterTagComponent, element: FilterTagDataCollection) {
        with(binding) {
            tgFilterTag.text = element.value
            checboxFilterTag.isChecked = element.isSelected

            root.setOnClickListener {
                checboxFilterTag.isChecked = !element.isSelected
                checkBoxListener.onCheckBoxClicked(tagComponent, element, position)
            }

            checboxFilterTag.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBoxListener.onCheckBoxClicked(tagComponent, element, position)
            }
        }
    }

    interface CheckBoxListener {
        fun onCheckBoxClicked(
            tagComponent: TelcoFilterTagComponent,
            element: FilterTagDataCollection, position: Int
        )
    }
}