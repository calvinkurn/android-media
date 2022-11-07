package com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.databinding.FilterPriceRangeItemBinding

internal class PriceRangeFilterCheckboxItemAdapter(
    private val priceRangeFilterCheckboxDataView: PriceRangeFilterCheckboxDataView,
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
        holder.bind(
            priceRangeFilterCheckboxDataView,
            priceRangeFilterCheckboxDataView.optionViewModelList[position],
            priceRangeFilterCheckboxDataView.optionViewModelList.size
        )
    }

    override fun getItemCount(): Int = priceRangeFilterCheckboxDataView.optionViewModelList.size
}