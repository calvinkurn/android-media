package com.tokopedia.logisticaddaddress.features.addeditaddress.search

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictSearchPageBinding

class AutoCompleteListAdapterViewHolder(private val binding: ItemDistrictSearchPageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: SuggestedPlace) {
        binding.searchPlaceName.text = data.mainText
        binding.searchPlaceAddress.text = data.secondaryText
    }

    companion object {
        fun getViewHolder(binding: ItemDistrictSearchPageBinding) = AutoCompleteListAdapterViewHolder(binding)
    }
}
