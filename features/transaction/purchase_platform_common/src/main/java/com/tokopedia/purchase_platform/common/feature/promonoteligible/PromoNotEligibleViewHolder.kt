package com.tokopedia.purchase_platform.common.feature.promonoteligible

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.databinding.ItemPromoRedStateBinding

/**
 * Created by Irfan Khoirul on 2019-06-21.
 */

class PromoNotEligibleViewHolder(private val binding: ItemPromoRedStateBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_promo_red_state
    }

    fun bind(model: NotEligiblePromoHolderdata) {
        if (model.showShopSection) {
            binding.imageMerchant.show()
            when (model.iconType) {
                NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL -> binding.imageMerchant.setImageResource(R.drawable.ic_promo_global)
                NotEligiblePromoHolderdata.TYPE_ICON_OFFICIAL_STORE -> binding.imageMerchant.setImageResource(R.drawable.ic_badge_shop_official)
                NotEligiblePromoHolderdata.TYPE_ICON_POWER_MERCHANT -> binding.imageMerchant.setImageResource(R.drawable.ic_power_merchant)
                else -> binding.imageMerchant.gone()
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