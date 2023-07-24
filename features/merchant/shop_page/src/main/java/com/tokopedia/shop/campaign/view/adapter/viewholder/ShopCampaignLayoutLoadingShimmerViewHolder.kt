package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R

class ShopCampaignLayoutLoadingShimmerViewHolder(view: View) : AbstractViewHolder<LoadingModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_page_campaign_shimmering
    }

    override fun bind(element: LoadingModel?) {}
}
