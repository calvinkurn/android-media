package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

class ShopCampaignMultipleImageColumnPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_shop_campaign_multiple_image_column_placeholder
    }

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {}
}
