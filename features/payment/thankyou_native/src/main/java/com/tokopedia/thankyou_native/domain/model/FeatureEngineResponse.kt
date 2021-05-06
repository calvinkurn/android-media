package com.tokopedia.thankyou_native.domain.model

import com.google.gson.annotations.SerializedName

data class FeatureEngineResponse(
        @SerializedName("validateEngineRequest")
        val validateEngineResponse: ValidateEngineResponse

)

data class ValidateEngineResponse(
        @SerializedName("success")
        val success: Boolean,
        @SerializedName("error_code")
        val errorCode: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("data")
        val engineData : FeatureEngineData?
)

data class FeatureEngineData (
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description : String,
        @SerializedName("items")
        val featureEngineItem : ArrayList<FeatureEngineItem>?
)

data class FeatureEngineItem (
        @SerializedName("id")
        val id: Long,
        @SerializedName("detail")
        val detail : String
)
