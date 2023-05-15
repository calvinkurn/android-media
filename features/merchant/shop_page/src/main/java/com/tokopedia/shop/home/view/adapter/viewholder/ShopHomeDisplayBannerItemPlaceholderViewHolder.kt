package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel

class ShopHomeDisplayBannerItemPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<ShopHomeNewProductLaunchCampaignUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_campaign_placeholder
    }

    override fun bind(model: ShopHomeNewProductLaunchCampaignUiModel) {
    }
}
