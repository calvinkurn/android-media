package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.sellerorder.databinding.ItemSomDetailMvcUsageBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.model.MVCUsageUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailMVCUsageViewHolder(
    itemView: View
) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    private val binding by viewBinding<ItemSomDetailMvcUsageBinding>()

    override fun bind(item: SomDetailData, position: Int) {
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