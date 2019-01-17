package com.tokopedia.flashsale.management.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.EmptyMyCampaignViewHolder
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import com.tokopedia.flashsale.management.view.viewmodel.EmptyMyCampaignViewModel

class CampaignAdapterTypeFactory : BaseAdapterTypeFactory(), CampaignAdapterTypeFactoryContract {

    override fun type(viewModel: CampaignViewModel): Int {
        return CampaignViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyMyCampaignViewModel): Int {
        return EmptyMyCampaignViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CampaignViewHolder.LAYOUT -> CampaignViewHolder(parent)
            EmptyMyCampaignViewHolder.LAYOUT -> EmptyMyCampaignViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}