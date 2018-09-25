package com.tokopedia.flashsale.management.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import kotlinx.android.synthetic.main.item_campaign.view.*

class CampaignViewHolder(itemView: View) : AbstractViewHolder<CampaignViewModel>(itemView) {

    override fun bind(campaignViewModel: CampaignViewModel) {
        itemView.tvCampaignType.text = campaignViewModel.campaign_type
        itemView.tvCampaignName.text = campaignViewModel.name
        itemView.tvCampaignDate.text = campaignViewModel.campaign_period
        itemView.tvStatus.text = campaignViewModel.status
        ImageHandler.loadImageRounded2(itemView.context, itemView.ivImageCampaign, campaignViewModel.cover, 20f)
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_campaign
    }
}
