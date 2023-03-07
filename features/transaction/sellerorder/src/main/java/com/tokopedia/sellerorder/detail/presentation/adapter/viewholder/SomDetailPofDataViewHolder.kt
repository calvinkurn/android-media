package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSomDetailMvcUsageBinding
import com.tokopedia.sellerorder.databinding.ItemSomDetailPofDataBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.presentation.model.MVCUsageUiModel
import com.tokopedia.sellerorder.detail.presentation.model.PofDataUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailPofDataViewHolder(
    itemView: View?
) : AbstractViewHolder<SomDetailData>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_detail_mvc_usage
    }

    private val binding by viewBinding<ItemSomDetailPofDataBinding>()

    override fun bind(item: SomDetailData) {
        val data = item.dataObject
        if (data is PofDataUiModel) {
            binding?.run {
                setupHeader(data.header)
                setupFooter(data.footer)
                setupLabel(data.label)
                setupValue(data.value)
            }
        }
    }

    private fun ItemSomDetailPofDataBinding.setupHeader(header: String) {
        tvPofDataHeader.text = header
    }

    private fun ItemSomDetailPofDataBinding.setupFooter(footer: String) {
        tvPofDataFooter.text = footer
    }

    private fun ItemSomDetailPofDataBinding.setupLabel(label: String) {
        tvPofDataLabel.text = label
    }

    private fun ItemSomDetailPofDataBinding.setupValue(value: String) {
        tvPofDataValue.text = value
    }
}
