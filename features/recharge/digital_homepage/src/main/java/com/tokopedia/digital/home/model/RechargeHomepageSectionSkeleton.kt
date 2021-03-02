package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeHomepageSectionSkeleton (
        @SerializedName("search_bar_placeholder")
        @Expose
        val searchBarPlaceholder: String = "",

        @SerializedName("search_bar_app_link")
        @Expose
        val searchBarAppLink: String = "",

        @SerializedName("search_bar_web_link")
        @Expose
        val searchBarWebLink: String = "",

        @SerializedName("sections")
        @Expose
        val sections: List<Item> = listOf()
) {
    data class Response (
            @SerializedName("rechargeGetDynamicPageSkeleton")
            @Expose
            val response: RechargeHomepageSectionSkeleton = RechargeHomepageSectionSkeleton()
    )

    data class Item(
            @SerializedName("id")
            @Expose
            val id: String = "",
            @SerializedName("template")
            @Expose
            val template: String = ""
    )
}