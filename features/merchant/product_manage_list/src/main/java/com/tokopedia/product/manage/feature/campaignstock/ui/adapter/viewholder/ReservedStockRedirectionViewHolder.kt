package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemCampaignStockRedirectionBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockRedirectionUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.CampaignStock
import com.tokopedia.utils.view.binding.viewBinding

class ReservedStockRedirectionViewHolder(itemView: View?): AbstractViewHolder<ReservedStockRedirectionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_redirection
    }

    private val binding by viewBinding<ItemCampaignStockRedirectionBinding>()

    override fun bind(element: ReservedStockRedirectionUiModel?) {
        binding?.imgCampaignStockRedirection?.setImageUrl(CampaignStock.REDIRECTION_IMAGE_URL)
    }
}