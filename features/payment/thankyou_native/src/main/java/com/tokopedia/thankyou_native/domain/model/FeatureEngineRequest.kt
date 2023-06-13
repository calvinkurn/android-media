package com.tokopedia.thankyou_native.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue

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
        val parameters: @RawValue Map<String, Any?>? = null,
        @SerializedName("operators")
        val operators : FeatureEngineRequestOperators,
        @SerializedName("thresholds")
        val thresholds : FeatureEngineRequestThresholds
)


class FeatureEngineRequestOperators
class FeatureEngineRequestThresholds
