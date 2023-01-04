package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel

class ShopCampaignNplPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<ShopHomeNewProductLaunchCampaignUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_new_product_launch_placeholder
    }

    override fun bind(model: ShopHomeNewProductLaunchCampaignUiModel) {
    }
}
