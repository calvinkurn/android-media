package com.tokopedia.product.detail.data.model

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage

data class ProductInfoP2Login (
        var isWishlisted: Boolean = false,
        var pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate? = null,
        var topAdsGetProductManage: TopAdsGetProductManage = TopAdsGetProductManage()
)