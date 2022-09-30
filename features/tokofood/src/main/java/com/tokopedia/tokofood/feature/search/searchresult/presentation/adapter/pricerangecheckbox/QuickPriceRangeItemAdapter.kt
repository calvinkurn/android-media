package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.pricerangecheckbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.ItemTokofoodSearchPriceRangeBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeFilterCheckboxItemUiModel

class QuickPriceRangeItemAdapter(
    private val uiModels: List<PriceRangeFilterCheckboxItemUiModel>,
    private val quickPriceRangeFilterListener: QuickPriceRangeFilterListener
) : RecyclerView.Adapter<QuickPriceRangeFilterItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuickPriceRangeFilterItemViewHolder {
        val binding = ItemTokofoodSearchPriceRangeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuickPriceRangeFilterItemViewHolder(
            binding,
            quickPriceRangeFilterListener
        )
    }

    override fun onBindViewHolder(holder: QuickPriceRangeFilterItemViewHolder, position: Int) {
        holder.bind(
            uiModels[position],
            uiModels.size
        )
    }

    override fun getItemCount(): Int = uiModels.size
}