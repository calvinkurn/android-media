package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopoints.view.model.merchantcoupon.CatalogMVCWithProductsListItem

data class LayoutMerchantCouponAttr(
        @SerializedName("topAdsJsonParam")
        val topAdsJsonParam: String? = null,

        @SerializedName("CatalogMVCWithProductsList")
        val catalogMVCWithProductsList: List<CatalogMVCWithProductsListItem?>? = null
)