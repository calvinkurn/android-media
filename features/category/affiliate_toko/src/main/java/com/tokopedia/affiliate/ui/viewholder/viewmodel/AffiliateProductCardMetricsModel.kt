package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.sse.model.AffiliateSSEAdpTotalClickItem
import kotlinx.coroutines.flow.StateFlow

class AffiliateProductCardMetricsModel(
    val metrics: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric?,
    val product: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item,
    val affiliateSSEAdpTotalClickItem: StateFlow<AffiliateSSEAdpTotalClickItem?>
) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
