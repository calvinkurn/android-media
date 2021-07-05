package com.tokopedia.notifcenter.data.entity.orderlist


import com.google.gson.annotations.SerializedName

data class Card(
        @SerializedName("counter_str")
        var counter: String = "0",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("link")
        val link: Link = Link(),
        @SerializedName("text")
        val text: String = ""
) {
        fun hasCounter(): Boolean {
                return counter.isNotEmpty() && counter != "0"
        }
}