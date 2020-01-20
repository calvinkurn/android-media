package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage

data class ProductInfoP1(
        var productInfo: ProductInfo = ProductInfo(),
        var topAdsGetProductManage: TopAdsGetProductManage = TopAdsGetProductManage()
)