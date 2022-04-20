package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData

class AffiliateTrafficAttributionModel(val metrics: AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics.Tooltip.SubMetrics?) : Visitable<AffiliateBottomSheetTypeFactory> {

    override fun type(typeFactory: AffiliateBottomSheetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
