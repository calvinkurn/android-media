package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

class ShopCampaignSliderBannerPlaceholderViewHolder(
    view: View?
) : AbstractViewHolder<BaseShopHomeWidgetUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_campaign_slider_banner_placeholder
    }

    override fun bind(uiModel: BaseShopHomeWidgetUiModel) {}
}
