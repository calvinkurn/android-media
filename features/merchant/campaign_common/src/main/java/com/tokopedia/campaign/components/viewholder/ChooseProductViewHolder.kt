package com.tokopedia.campaign.components.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.ChooseProductDelegateAdapter
import com.tokopedia.campaign.databinding.ItemChooseProductBinding
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setViewGroupEnabled
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

class ChooseProductViewHolder(
    private val binding: ItemChooseProductBinding,
    private val listener: ChooseProductDelegateAdapter.ChooseProductListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val OPACITY_ACTIVE = 1f
        const val OPACITY_INACTIVE = 0.5f
    }

    init {
        binding.root.setOnClickListener {
            binding.checkboxItem.apply {
                isChecked = !isChecked
                callOnClick()
            }
        }
    }

    fun bind(param: ChooseProductDelegateAdapter.AdapterParam) {
        val item = param.item
        binding.apply {
            setItemInability(param, tvDisabledReason)
            imgProduct.loadImage(item.imageUrl)
            tvProductName.text = item.productName
            labelVariantCount.text = item.variantText
            tvProductPrice.text = item.priceText
            tvVariantTips.text = item.variantTips
            tvStock.text = item.stockText
            tvCheckDetail.isVisible = item.showCheckDetailCta
            checkboxItem.isChecked = item.isSelected
            labelVariantCount.isVisible = item.hasVariant
            tvVariantTips.isVisible = item.hasVariant

            tvCheckDetail.setOnClickListener {
                listener?.onDetailClicked(adapterPosition, item)
            }
            checkboxItem.setOnClickListener {
                item.isSelected = checkboxItem.isChecked
                listener?.onChooseProductClicked(adapterPosition, item)
            }
        }
    }

    private fun setItemInability(
        param: ChooseProductDelegateAdapter.AdapterParam,
        tvDisabledReason: Typography
    ) {
        val enableSelection = param.enableSelection
        val item = param.item
        if (item.isEnabled && !item.isSelected) {
            setEnable(enableSelection, binding)
            tvDisabledReason.text = param.errorMessage
        } else {
            setEnable(item.isEnabled, binding)
            tvDisabledReason.text = item.errorMessage
        }
    }

    private fun setEnable(enabled: Boolean, binding: ItemChooseProductBinding) {
        binding.apply {
            imgProduct.alpha = if (enabled) OPACITY_ACTIVE else OPACITY_INACTIVE
            root.isEnabled = enabled
            root.setViewGroupEnabled(enabled)
            tvCheckDetail.enable() // this text/cta will always enabled
            tvDisabledReason.enable() // this must enabled to be highlighted in red
        }
    }
}