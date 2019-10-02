package com.tokopedia.globalnavwidget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GlobalNavWidgetModel(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("keyword")
        @Expose
        val keyword: String = "",
        @SerializedName("nav_template")
        @Expose
        val navTemplate: String = "",
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
            val clickItemUrl: String = ""
    )
}