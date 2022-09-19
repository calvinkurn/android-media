package com.tokopedia.filter.bottomsheet.pricerangecheckbox.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.databinding.FilterPriceRangeItemBinding

class PriceRangeFilterCheckboxItemAdapter(
    private val priceRangeFilterList: List<PriceRangeFilterCheckboxItemUiModel>,
    private val priceRangeFilterCheckboxListener: PriceRangeFilterCheckboxListener
) : RecyclerView.Adapter<PriceRangeFilterCheckboxItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PriceRangeFilterCheckboxItemViewHolder {
        val binding = FilterPriceRangeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PriceRangeFilterCheckboxItemViewHolder(
            binding,
            priceRangeFilterCheckboxListener
        )
    }

    override fun onBindViewHolder(holder: PriceRangeFilterCheckboxItemViewHolder, position: Int) {
        holder.bind(priceRangeFilterList[position], priceRangeFilterList.size)
    }

    override fun getItemCount(): Int = priceRangeFilterList.size
}