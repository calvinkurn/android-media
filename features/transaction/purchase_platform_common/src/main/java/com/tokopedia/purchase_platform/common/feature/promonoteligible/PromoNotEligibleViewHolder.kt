package com.tokopedia.purchase_platform.common.feature.promonoteligible

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.databinding.ItemPromoRedStateBinding
import com.tokopedia.utils.image.ImageUtils

class PromoNotEligibleViewHolder(private val binding: ItemPromoRedStateBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_promo_red_state
    }

    fun bind(model: NotEligiblePromoHolderdata) {
        if (model.showShopSection) {
            binding.imageMerchant.show()
            if (model.shopBadge.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholder(binding.imageMerchant, model.shopBadge)
            } else if (model.iconType == NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL) {
                binding.imageMerchant.setImageResource(R.drawable.ic_promo_global)
            } else {
                binding.imageMerchant.gone()
            }
        } else {
            binding.imageMerchant.gone()
        }
        binding.labelMerchant.text = model.shopName
        if (model.promoTitle.isNotBlank()) {
            binding.labelPromoTitle.text = model.promoTitle
            binding.labelPromoTitle.show()
        } else {
            binding.labelPromoTitle.gone()
        }

        if (model.errorMessage.isNotBlank()) {
            binding.labelPromoErrorMessage.text = model.errorMessage
            binding.labelPromoErrorMessage.show()
        } else {
            binding.labelPromoErrorMessage.gone()
        }
    }

}