package com.tokopedia.exploreCategory.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.exploreCategory.model.AffiliateProductCommissionData

class AffiliatePromotionErrorCardVHViewModel(val error : AffiliateProductCommissionData.Error) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
