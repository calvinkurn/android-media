package com.tokopedia.logisticaddaddress.features.district_recommendation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictRecommendationRevampBinding
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.unifycomponents.HtmlLinkHelper

class DiscomAdapterViewHolder (private val binding: ItemDistrictRecommendationRevampBinding): RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: Address, keyword: String) {
        when {
            data.cityName.contains(keyword,ignoreCase = true) -> {
                binding.searchPlaceName.text = HtmlLinkHelper(itemView.context, itemView.context.getString(R.string.tv_discom_item_revamp_city, data.provinceName, data.cityName, data.districtName)).spannedString
            }
            data.districtName.contains(keyword, ignoreCase = true) -> {
                binding.searchPlaceName.text = HtmlLinkHelper(itemView.context, itemView.context.getString(R.string.tv_discom_item_revamp_district, data.provinceName, data.cityName, data.districtName)).spannedString
            }
            else -> {
                binding.searchPlaceName.text = HtmlLinkHelper(itemView.context, itemView.context.getString(R.string.tv_discom_item_revamp_city, data.provinceName, data.cityName, data.districtName)).spannedString
            }
        }
    }

    companion object {
        fun getViewHolder(binding: ItemDistrictRecommendationRevampBinding) = DiscomAdapterViewHolder(binding)
    }
}