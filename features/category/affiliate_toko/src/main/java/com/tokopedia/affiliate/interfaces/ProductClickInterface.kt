package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams

interface ProductClickInterface {
    fun onProductClick(
        productId: String,
        productName: String,
        productImage: String,
        productUrl: String,
        productIdentifier: String,
        status: Int?,
        type: String? = null,
        ssaInfo: AffiliatePromotionBottomSheetParams.SSAInfo? = null,
        imageArray: List<String?>? = null
    )
}
