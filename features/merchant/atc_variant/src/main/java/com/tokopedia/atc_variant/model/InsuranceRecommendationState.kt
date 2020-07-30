package com.tokopedia.atc_variant.model

import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceRecommendationGqlResponse

sealed class InsuranceRecommendationState

data class ProductInfoAndVariantContainer(var productInfoAndVariant: ProductInfoAndVariant = ProductInfoAndVariant()) : InsuranceRecommendationState()
data class InsuranceRecommendationContainer(var insuranceRecommendation: InsuranceRecommendationGqlResponse = InsuranceRecommendationGqlResponse()) : InsuranceRecommendationState()
data class Fail(var throwable: Throwable) : InsuranceRecommendationState()