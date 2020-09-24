package com.tokopedia.thankyou_native.domain.model

import com.google.gson.annotations.SerializedName

data class FeatureEngineRequest (
        @SerializedName("merchant_code")
        val merchantCode : String,
        @SerializedName("profile_code")
        val profileCode : String,
        @SerializedName("engine_id")
        val engineId : Int,
        @SerializedName("limit")
        val limit : Int,
        @SerializedName("parameters")
        val parameters : FeatureEngineRequestParameters,
        @SerializedName("operators")
        val operators : FeatureEngineRequestOperators,
        @SerializedName("thresholds")
        val thresholds : FeatureEngineRequestThresholds
)

data class FeatureEngineRequestParameters(
        @SerializedName("amount")
        val amount : String
)

class FeatureEngineRequestOperators
class FeatureEngineRequestThresholds