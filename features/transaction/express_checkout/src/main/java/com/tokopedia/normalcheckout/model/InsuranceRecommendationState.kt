package com.tokopedia.normalcheckout.model

import com.tokopedia.transactiondata.insurance.entity.response.InsuranceRecommendationGqlResponse

sealed class InsuranceRecommendationState

data class ProductInfoAndVariantContainer(var productInfoAndVariant: ProductInfoAndVariant = ProductInfoAndVariant()) : InsuranceRecommendationState()
data class InsuranceRecommendationContainer(var insuranceRecommendation: InsuranceRecommendationGqlResponse = InsuranceRecommendationGqlResponse()) : InsuranceRecommendationState()
data class Fail(var throwable: Throwable) : InsuranceRecommendationState()