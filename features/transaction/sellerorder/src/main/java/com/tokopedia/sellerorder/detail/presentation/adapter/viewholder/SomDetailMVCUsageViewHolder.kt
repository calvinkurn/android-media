package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSomDetailMvcUsageBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.presentation.model.MVCUsageUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailMVCUsageViewHolder(
    itemView: View?
) : AbstractViewHolder<SomDetailData>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_detail_mvc_usage
    }

    private val binding by viewBinding<ItemSomDetailMvcUsageBinding>()

    override fun bind(item: SomDetailData) {
        val data = item.dataObject
        if (data is MVCUsageUiModel) {
            setupDescription(data.description)
            setupValue(data.value, data.valueDetail)
        }
    }

    private fun setupDescription(description: String) {
        binding?.tvMvcUsageDescription?.text = description
    }

    private fun setupValue(value: String, valueDetail: String) {
        binding?.tvMvcUsageValue?.text = value
        binding?.tvMvcUsageValueDetail?.text = valueDetail
    }
}