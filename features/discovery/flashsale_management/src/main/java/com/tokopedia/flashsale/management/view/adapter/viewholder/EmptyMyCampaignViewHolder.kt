package com.tokopedia.flashsale.management.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import com.tokopedia.flashsale.management.view.viewmodel.EmptyMyCampaignViewModel
import kotlinx.android.synthetic.main.item_campaign.view.*

class EmptyMyCampaignViewHolder(itemView: View) : AbstractViewHolder<EmptyMyCampaignViewModel>(itemView) {

    override fun bind(emptyMyCampaignViewModel: EmptyMyCampaignViewModel) {
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_empty_my_campaign
    }
}
