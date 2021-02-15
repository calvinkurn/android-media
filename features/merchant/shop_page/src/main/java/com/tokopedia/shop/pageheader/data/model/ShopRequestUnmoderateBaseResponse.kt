package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopRequestUnmoderateBaseResponse(
        @SerializedName("errors")
        @Expose
        val errors : List<ShopRequestUnmoderateErrorResponse> = listOf(),
        @SerializedName("data")
        @Expose
        val data : ShopRequestUnmoderateSuccessResponse = ShopRequestUnmoderateSuccessResponse()
)

data class ShopRequestUnmoderateErrorResponse(
        @SerializedName("message")
        @Expose
        val message : String = "",
        @SerializedName("path")
        @Expose
        val path : List<String>? = listOf(),
        @SerializedName("extensions")
        @Expose
        val extensions : ShopRequestUnmoderateErrorResponseExtensions = ShopRequestUnmoderateErrorResponseExtensions()
)

data class ShopRequestUnmoderateErrorResponseExtensions(
        @SerializedName("developerMessage")
        @Expose
        val developerMessage : String = "",
        @SerializedName("moreInfo")
        @Expose
        val moreInfo : String = ""
)

data class ShopRequestUnmoderateSuccessResponse(
        @SerializedName("moderateShop")
        @Expose
        val moderateShop : ShopRequestUnmoderateDataModel? = ShopRequestUnmoderateDataModel()
)

data class ShopRequestUnmoderateDataModel(
        @SerializedName("success")
        @Expose
        val success : Boolean = false,
        @SerializedName("message")
        @Expose
        val message : String = ""
)