package com.tokopedia.tokomart.category.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokonowCategoryDetail(
        @SerializedName("TokonowCategoryDetail")
        @Expose
        val categoryDetail: CategoryDetail = CategoryDetail(),
) {

    data class CategoryDetail(
            @SerializedName("header")
            @Expose
            val header: Header = Header(),

            @SerializedName("data")
            @Expose
            val data: Data = Data(),
    )

    data class Header(
            @SerializedName("process_time")
            @Expose
            val processTime: Double = 0.0,

            @SerializedName("messages")
            @Expose
            val messages: List<String> = listOf(),

            @SerializedName("reason")
            @Expose
            val reason: String = "",

            @SerializedName("error_code")
            @Expose
            val errorCode: String = "",
    )

    data class Data(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applinks")
            @Expose
            val applinks: String = "",

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",

            @SerializedName("navigation")
            @Expose
            val navigation: Navigation = Navigation(),
    )

    data class Navigation(
            @SerializedName("prev")
            @Expose
            val prev: NavigationItem = NavigationItem(),

            @SerializedName("next")
            @Expose
            val next: NavigationItem = NavigationItem(),
    )

    data class NavigationItem(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applinks")
            @Expose
            val applinks: String = "",

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",
    )
}