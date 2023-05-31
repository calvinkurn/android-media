package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

class ShopCampaignVoucherSliderPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<BaseShopHomeWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_campaign_voucher_slider_widget_placeholder
    }

    override fun bind(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel) {}
}
