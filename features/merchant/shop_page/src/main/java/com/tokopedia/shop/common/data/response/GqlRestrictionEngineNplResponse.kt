package com.tokopedia.shop.common.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlRestrictionEngineNplResponse (
        @SerializedName("data")
        @Expose
        val data : RestrictionEngineData
)

data class RestrictionEngineData (
        @SerializedName("restrictValidateRestriction")
        @Expose
        val restrictValidateRestriction : RestrictValidateRestriction
)

data class RestrictValidateRestriction (
        @SerializedName("success")
        @Expose
        val success : Boolean = false,
        @SerializedName("message")
        @Expose
        val message : String = "",
        @SerializedName("dataResponse")
        @Expose
        val dataResponse : List<RestrictionEngineDataResponse>
)

data class RestrictionEngineDataResponse (
        @SerializedName("productID")
        @Expose
        val productId : String = "",
        @SerializedName("status")
        @Expose
        val status : String = "",
        @SerializedName("actions")
        @Expose
        val actions : List<Actions>
)

data class Actions (
        @SerializedName("actionType")
        @Expose
        val actionType : String = "",
        @SerializedName("title")
        @Expose
        val title : String = "",
        @SerializedName("description")
        @Expose
        val description : String = "",
        @SerializedName("actionURL")
        @Expose
        val actionUrl : String = "",
        @SerializedName("attributeName")
        @Expose
        val attributeName : String = ""
)