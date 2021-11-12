package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.AffiliateDateFilterData
import com.tokopedia.affiliate.model.AffiliateHomeHeaderData
import com.tokopedia.affiliate.model.AffiliateTermsAndConditionData

class AffiliateDateFilterModel(val data: AffiliateDateFilterData) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
