package com.tokopedia.filter.bottomsheet.pricerangecheckbox.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.PriceRangeFilterCheckboxListener
import com.tokopedia.filter.databinding.FilterPriceRangeItemBinding

class PriceRangeCbItemAdapter(
    private val priceRangeFilterList: List<PriceRangeFilterCheckboxItemUiModel>,
    private val priceRangeFilterCheckboxListener: PriceRangeFilterCheckboxListener
) : RecyclerView.Adapter<PriceRangeCbItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PriceRangeCbItemViewHolder {
        val binding = FilterPriceRangeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PriceRangeCbItemViewHolder(
            binding,
            priceRangeFilterCheckboxListener
        )
    }

    override fun onBindViewHolder(holder: PriceRangeCbItemViewHolder, position: Int) {
        holder.bind(priceRangeFilterList[position], priceRangeFilterList.size)
    }

    override fun getItemCount(): Int = priceRangeFilterList.size
}