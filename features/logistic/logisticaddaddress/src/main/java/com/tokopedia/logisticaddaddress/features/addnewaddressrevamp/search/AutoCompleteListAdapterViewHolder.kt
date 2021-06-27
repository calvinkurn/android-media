package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictSearchPageBinding
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil

class AutoCompleteListAdapterViewHolder(binding: ItemDistrictSearchPageBinding): RecyclerView.ViewHolder(binding.root) {
    private val placeName = binding.searchPlaceName
    private val placeAddress = binding.searchPlaceAddress

    fun bindData(data: SuggestedPlace) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(placeName, data.mainText, placeName.context.getString(R.string.content_desc_place_name))
        placeAddress.text = data.secondaryText
    }

    companion object {
        fun getViewHolder(binding: ItemDistrictSearchPageBinding) = AutoCompleteListAdapterViewHolder(binding)
    }
}