package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockRedirectionUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.CampaignStock
import kotlinx.android.synthetic.main.item_campaign_stock_redirection.view.*

class ReservedStockRedirectionViewHolder(itemView: View?): AbstractViewHolder<ReservedStockRedirectionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_redirection
    }

    override fun bind(element: ReservedStockRedirectionUiModel?) {
        itemView.img_campaign_stock_redirection?.setImageUrl(CampaignStock.REDIRECTION_IMAGE_URL)
    }
}