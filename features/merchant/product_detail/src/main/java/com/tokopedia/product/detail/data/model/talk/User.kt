package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("admin_status")
        @Expose
        val adminStatus: String = "",

        @SerializedName("full_name")
        @Expose
        val fullName: String = "",

        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("reputation")
        @Expose
        val reputation: Reputation = Reputation(),

        @SerializedName("shop")
        @Expose
        val shop: ProductTalkShop = ProductTalkShop(),

        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = ""
)