package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData

interface AffiliateInfoClickInterfaces {
    fun onInfoClick(title: String?, desc: String?, advanceTooltip: List<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail.Tooltip?>?)
}