package com.tokopedia.flashsale.management.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.EmptyMyCampaignViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignStatusListViewHolder
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import com.tokopedia.flashsale.management.view.viewmodel.EmptyMyCampaignViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusListViewModel

class CampaignAdapterTypeFactory() : BaseAdapterTypeFactory(), CampaignAdapterTypeFactoryContract {

    private lateinit var onCampaignStatusListViewHolderListener: CampaignStatusListViewHolder.OnCampaignStatusListViewHolderListener

    constructor(onCampaignStatusListViewHolderListener: CampaignStatusListViewHolder.OnCampaignStatusListViewHolderListener) : this() {
        this.onCampaignStatusListViewHolderListener = onCampaignStatusListViewHolderListener
    }

    override fun type(viewModel: CampaignViewModel): Int {
        return CampaignViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyMyCampaignViewModel): Int {
        return EmptyMyCampaignViewHolder.LAYOUT
    }

    override fun type(viewModel: CampaignStatusListViewModel): Int {
        return CampaignStatusListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CampaignViewHolder.LAYOUT -> CampaignViewHolder(parent)
            EmptyMyCampaignViewHolder.LAYOUT -> EmptyMyCampaignViewHolder(parent)
            CampaignStatusListViewHolder.LAYOUT -> CampaignStatusListViewHolder(parent, onCampaignStatusListViewHolderListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}