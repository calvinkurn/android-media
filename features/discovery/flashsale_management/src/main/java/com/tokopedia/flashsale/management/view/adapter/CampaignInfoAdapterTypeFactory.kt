package com.tokopedia.flashsale.management.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail.*
import com.tokopedia.flashsale.management.view.viewmodel.*

class CampaignInfoAdapterTypeFactory(val sellerStatus: SellerStatus,
                                     val onClickProductList: () -> Unit): BaseAdapterTypeFactory(){

    fun type(model: CampaignInfoViewModel): Int {
        return when(model){
            is CampaignInfoHeaderViewModel -> CampaignInfoHeaderViewHolder.LAYOUT
            is CampaignInfoCategoryViewModel -> CampaignInfoCategoryViewHolder.LAYOUT
            is CampaignInfoSectionViewModel -> CampaignInfoSectionViewHolder.LAYOUT
            is CampaignInfoShopCriteriaViewModel -> CampaignInfoShopCriteriaViewHolder.LAYOUT
            is CampaignInfoDescriptionViewModel -> CampaignInfoDescriptionViewHolder.LAYOUT
            is CampaignInfoPromoViewModel -> CampaignInfoPromoViewHolder.LAYOUT
            is CampaignInfoTnCViewModel -> CampaignInfoTnCViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            CampaignInfoHeaderViewHolder.LAYOUT -> CampaignInfoHeaderViewHolder(parent, sellerStatus, onClickProductList)
            CampaignInfoCategoryViewHolder.LAYOUT -> CampaignInfoCategoryViewHolder(parent)
            CampaignInfoSectionViewHolder.LAYOUT -> CampaignInfoSectionViewHolder(parent)
            CampaignInfoShopCriteriaViewHolder.LAYOUT -> CampaignInfoShopCriteriaViewHolder(parent)
            CampaignInfoDescriptionViewHolder.LAYOUT -> CampaignInfoDescriptionViewHolder(parent)
            CampaignInfoPromoViewHolder.LAYOUT -> CampaignInfoPromoViewHolder(parent)
            CampaignInfoTnCViewHolder.LAYOUT -> CampaignInfoTnCViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}