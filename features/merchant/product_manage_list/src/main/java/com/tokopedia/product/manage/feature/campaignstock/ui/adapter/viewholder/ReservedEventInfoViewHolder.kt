package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.databinding.ItemCampaignStockInfoCardBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.helper.VariantReservedViewHolderHelper.bind
import com.tokopedia.utils.view.binding.viewBinding

class ReservedEventInfoViewHolder(itemView: View?): AbstractViewHolder<ReservedEventInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_info_card
    }

    private val binding by viewBinding<ItemCampaignStockInfoCardBinding>()

    override fun bind(element: ReservedEventInfoUiModel) {
        itemView.context?.let {
            binding?.bind(it, element)
        }
    }
}