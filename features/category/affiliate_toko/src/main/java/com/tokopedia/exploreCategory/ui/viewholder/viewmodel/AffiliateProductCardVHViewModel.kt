package com.tokopedia.exploreCategory.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.exploreCategory.model.AffiliatePerformanceData

class AffiliateProductCardVHViewModel(val product: AffiliatePerformanceData.AffiliatePerformance.Data.Links.Item) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
