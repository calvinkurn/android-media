package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName
import com.tokopedia.mvcwidget.multishopmvc.data.CatalogMVCWithProductsListItem

data class LayoutMerchantCouponAttr(
        @SerializedName("topAdsJsonParam")
        val topAdsJsonParam: String = "",

        @SerializedName("CatalogMVCWithProductsList")
        val catalogMVCWithProductsList: List<CatalogMVCWithProductsListItem> = listOf()
)
