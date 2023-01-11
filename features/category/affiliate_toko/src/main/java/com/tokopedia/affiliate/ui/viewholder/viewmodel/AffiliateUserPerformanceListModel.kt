package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.sse.model.AffiliateSSEAdpTotalClick
import kotlinx.coroutines.flow.StateFlow

class AffiliateUserPerformanceListModel(
    val data: AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics?,
    val totalClick: StateFlow<AffiliateSSEAdpTotalClick?>?
) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
