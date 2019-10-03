package com.tokopedia.globalnavwidget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GlobalNavWidgetModel(
        @SerializedName("source")
        @Expose
        val source: String = "",
        @SerializedName("keyword")
        @Expose
        val keyword: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("nav_template")
        @Expose
        val navTemplate: String = "",
        @SerializedName("background")
        @Expose
        val background: String = "",
        @SerializedName("logo_url")
        @Expose
        val logoUrl: String = "",
        @SerializedName("reputation_url")
        @Expose
        val reputationUrl: String = "",
        @SerializedName("image_badge_url")
        @Expose
        val imageBadgeUrl: String = "",
        @SerializedName("credibility")
        @Expose
        val credibility: String = "",
        @SerializedName("is_official")
        @Expose
        val isOfficial: Boolean = false,
        @SerializedName("is_power_merchant")
        @Expose
        val isPowerMerchant: Boolean = false,
        @SerializedName("see_all_applink")
        @Expose
        val clickSeeAllApplink: String = "",
        @SerializedName("see_all_url")
        @Expose
        val clickSeeAllUrl: String = "",
        @SerializedName("list")
        @Expose
        val itemList: List<Item> = listOf()
) {

    data class Item(
            @SerializedName("name")
            @Expose
            val name: String = "",
            @SerializedName("info")
            @Expose
            val info: String = "",
            @SerializedName("image_url")
            @Expose
            val imageUrl: String = "",
            @SerializedName("applink")
            @Expose
            val clickItemApplink: String = "",
            @SerializedName("url")
            @Expose
            val clickItemUrl: String = "",
            @SerializedName("subtitle")
            @Expose
            val subtitle: String = "",
            @SerializedName("strikethrough")
            @Expose
            val strikethrough: String = "",
            @SerializedName("background_url")
            @Expose
            val backgroundUrl: String = "",
            @SerializedName("logoUrl")
            @Expose
            val logoUrl: String = ""
    )
}