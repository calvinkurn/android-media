package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

class ShopCampaignDisplaySliderBannerHighlightPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<BaseShopHomeWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_slider_banner_highlight_placeholder
    }

    override fun bind(uiModel: BaseShopHomeWidgetUiModel) {}
}
