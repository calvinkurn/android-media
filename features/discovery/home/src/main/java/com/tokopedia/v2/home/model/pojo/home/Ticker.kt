package com.tokopedia.v2.home.model.pojo.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ticker (
        @SerializedName("tickers")
    @Expose
    val tickers: List<Tickers> = listOf(),

        @SerializedName("meta")
    @Expose
    val meta: Meta
)

data class Tickers(
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = "",

        @SerializedName("created_by")
        @Expose
        val createdBy: String = "",
        
        @SerializedName("created_on")
        @Expose
        val createdOn: String = "",
        
        @SerializedName("expire_time")
        @Expose
        val expireTime: String = "",
        
        @SerializedName("id")
        @Expose
        val id: String = "-1",
        
        @SerializedName("message")
        @Expose
        val message: String = "",
        
        @SerializedName("title")
        @Expose
        val title: String = "",
        
        @SerializedName("layout")
        @Expose
        val layout: String = "",
        
        @SerializedName("target")
        @Expose
        val target: String = "",
        
        @SerializedName("device")
        @Expose
        val device: String = "",
        
        @SerializedName("updated_on")
        @Expose
        val updatedOn: String = "",
        
        @SerializedName("updated_by")
        @Expose
        val updatedBy: String = "",
        
        @SerializedName("ticker_type")
        @Expose
        val ticker_type: String = "",
        
        @SerializedName("color")
        @Expose
        val color: String = "",
        
        @SerializedName("status")
        @Expose
        val status: String = ""
)

data class Meta(
        @SerializedName("total_data")
        @Expose
        val totalData: String
)