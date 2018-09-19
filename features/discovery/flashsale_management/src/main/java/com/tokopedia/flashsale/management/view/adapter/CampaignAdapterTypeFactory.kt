package com.tokopedia.flashsale.management.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignViewHolder
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel

class CampaignAdapterTypeFactory : BaseAdapterTypeFactory(), CampaignAdapterTypeFactoryContract {

    override fun type(viewModel: CampaignViewModel): Int {
        return CampaignViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CampaignViewHolder.LAYOUT -> CampaignViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}