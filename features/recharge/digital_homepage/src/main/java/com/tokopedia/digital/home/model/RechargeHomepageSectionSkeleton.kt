package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeHomepageSectionSkeleton (
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
            val id: Int = 0,
            @SerializedName("template")
            @Expose
            val template: String = ""
    )
}