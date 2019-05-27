package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

data class Shop(
        @SerializedName("app_url")
        val appUrl: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("clover")
        val clover: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_gold")
        val isGold: Boolean,
        @SerializedName("location")
        val location: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("reputation")
        val reputation: String,
        @SerializedName("url")
        val url: String
)