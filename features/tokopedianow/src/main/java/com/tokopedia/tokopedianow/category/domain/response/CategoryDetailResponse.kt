package com.tokopedia.tokopedianow.category.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class CategoryDetailResponse(
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

            @SerializedName("colorObj")
            @Expose
            val colorObj: ColorObj = ColorObj(),

            @SerializedName("recommendation")
            @Expose
            val recommendation: List<RecommendationItem> = listOf(),

            @SerializedName("child")
            @Expose
            val child: List<ChildItem> = listOf()
    )

    data class ColorObj(
        @SerializedName("hexLight")
        @Expose
        val hexLight: String = String.EMPTY,

        @SerializedName("hexDark")
        @Expose
        val hexDark: String = String.EMPTY
    )

    data class ChildItem(
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

        @SerializedName("isKyc")
        @Expose
        val isKyc: Boolean = false,

        @SerializedName("isAdult")
        @Expose
        val isAdult: Int = 0,

        @SerializedName("minAge")
        @Expose
        val minAge: Int = 0,

        @SerializedName("colorObj")
        @Expose
        val colorObj: ColorObj = ColorObj()
    )

    data class RecommendationItem(
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

        @SerializedName("colorObj")
        @Expose
        val colorObj: ColorObj = ColorObj(),
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
