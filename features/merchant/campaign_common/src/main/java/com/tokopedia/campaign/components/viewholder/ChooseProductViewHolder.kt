package com.tokopedia.campaign.components.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.ChooseProductDelegateAdapter
import com.tokopedia.campaign.databinding.ItemChooseProductBinding
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setViewGroupEnabled
import com.tokopedia.media.loader.loadImage

class ChooseProductViewHolder(
    private val binding: ItemChooseProductBinding,
    private val listener: ChooseProductDelegateAdapter.ChooseProductListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val OPACITY_ACTIVE = 1f
        const val OPACITY_INACTIVE = 0.5f
    }

    init {
        binding.tvCheckDetail.setOnClickListener {
            listener?.onDetailClicked(adapterPosition)
        }
        binding.root.setOnClickListener {
            binding.checkboxItem.apply {
                callOnClick()
                isChecked = !isChecked
            }
        }
    }

    fun bind(item: ChooseProductItem) {
        binding.apply {
            imgProduct.loadImage(item.imageUrl)
            tvProductName.text = item.productName
            labelVariantCount.text = item.variantText
            tvProductPrice.text = item.priceText
            tvDisabledReason.text = item.errorMessage
            tvVariantTips.text = item.variantTips
            tvStock.text = item.stockText
            tvCheckDetail.isVisible = item.showCheckDetailCta
            checkboxItem.isChecked = item.isSelected
            checkboxItem.setOnClickListener {
                item.isSelected = !checkboxItem.isChecked
                listener?.onChooseProductClicked(adapterPosition)
            }
            labelVariantCount.isVisible = item.hasVariant
            tvVariantTips.isVisible = item.hasVariant
            setEnable(item.isEnabled, binding)
        }
    }

    private fun setEnable(enabled: Boolean, binding: ItemChooseProductBinding) {
        binding.apply {
            imgProduct.alpha = if (enabled) OPACITY_ACTIVE else OPACITY_INACTIVE
            root.isEnabled = enabled
            root.setViewGroupEnabled(enabled)
            tvCheckDetail.enable() // this text/cta will always enabled
        }
    }
}