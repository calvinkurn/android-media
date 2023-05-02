package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateSearchData

interface PromotionClickInterface {
    fun onPromotionClick(
        itemID: String,
        itemName: String,
        itemImage: String,
        itemURL: String,
        position: Int,
        commison: String,
        status: String = "",
        type: String? = "",
        ssaInfo: AffiliatePromotionBottomSheetParams.SSAInfo? = null
    )

    fun onButtonClick(errorCta: AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?)
}
