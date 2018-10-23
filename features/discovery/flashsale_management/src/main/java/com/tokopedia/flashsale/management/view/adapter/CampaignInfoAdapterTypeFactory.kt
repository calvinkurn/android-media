package com.tokopedia.flashsale.management.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignInfoHeaderViewHolder
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignInfoCategoryViewHolder
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoCategoryViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoHeaderViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoViewModel

class CampaignInfoAdapterTypeFactory: BaseAdapterTypeFactory(){

    fun type(model: CampaignInfoViewModel): Int {
        return when(model){
            is CampaignInfoHeaderViewModel -> CampaignInfoHeaderViewHolder.LAYOUT
            is CampaignInfoCategoryViewModel -> CampaignInfoCategoryViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            CampaignInfoHeaderViewHolder.LAYOUT -> CampaignInfoHeaderViewHolder(parent)
            CampaignInfoCategoryViewHolder.LAYOUT -> CampaignInfoCategoryViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}