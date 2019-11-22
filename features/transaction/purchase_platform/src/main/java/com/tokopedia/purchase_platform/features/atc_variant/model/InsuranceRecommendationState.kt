package com.tokopedia.purchase_platform.features.atc_variant.model

import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceRecommendationGqlResponse

sealed class InsuranceRecommendationState

data class ProductInfoAndVariantContainer(var productInfoAndVariant: ProductInfoAndVariant = ProductInfoAndVariant()) : InsuranceRecommendationState()
data class InsuranceRecommendationContainer(var insuranceRecommendation: InsuranceRecommendationGqlResponse = InsuranceRecommendationGqlResponse()) : InsuranceRecommendationState()
data class Fail(var throwable: Throwable) : InsuranceRecommendationState()