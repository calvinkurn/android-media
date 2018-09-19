package com.tokopedia.flashsale.management.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel

class CampaignViewHolder(itemView: View) : AbstractViewHolder<CampaignViewModel>(itemView) {


    init {
    }

    override fun bind(campaignViewModel: CampaignViewModel) {

    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_campaign
    }
}
