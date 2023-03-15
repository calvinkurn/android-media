package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateDiscoveryCampaignResponse

class AffiliateDiscoBannerUiModel(
    val article: AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign.Data.Campaign
) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
