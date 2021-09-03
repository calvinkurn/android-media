package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.AffiliateProductCommissionData

class AffiliatePromotionErrorCardVHViewModel(val error : AffiliateProductCommissionData.Error) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
