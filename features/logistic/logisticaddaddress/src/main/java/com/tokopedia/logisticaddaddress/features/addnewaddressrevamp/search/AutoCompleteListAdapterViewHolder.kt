package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictSearchPageBinding
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil

class AutoCompleteListAdapterViewHolder(private val binding: ItemDistrictSearchPageBinding): RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: SuggestedPlace) {
        binding.searchPlaceName.text = data.mainText
        binding.searchPlaceAddress.text = data.secondaryText
    }

    companion object {
        fun getViewHolder(binding: ItemDistrictSearchPageBinding) = AutoCompleteListAdapterViewHolder(binding)
    }
}