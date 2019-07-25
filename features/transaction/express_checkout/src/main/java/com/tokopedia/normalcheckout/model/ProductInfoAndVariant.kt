package com.tokopedia.normalcheckout.model

import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceRecommendationGqlResponse

data class ProductInfoAndVariant(
        var productInfo: ProductInfo = ProductInfo(),
        var productVariant: ProductVariant = ProductVariant(),
        var insuranceRecommendation: InsuranceRecommendationGqlResponse = InsuranceRecommendationGqlResponse()
)