package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.AffiliatePerformanceData
import com.tokopedia.affiliate.model.AffiliateTermsAndConditionData
import com.tokopedia.affiliate.model.AffiliateUserPerformaData
import com.tokopedia.affiliate.model.AffiliateUserPerformaListItemData

class AffiliateUserPerformanceListModel(val data: AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics?) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
