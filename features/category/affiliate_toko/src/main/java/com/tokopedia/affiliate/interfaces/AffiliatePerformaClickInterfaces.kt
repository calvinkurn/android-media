package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData

interface AffiliatePerformaClickInterfaces {
    fun onInfoClick(title: String?, desc: String?, metrics: List<AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics.Tooltip.SubMetrics?>?)
}