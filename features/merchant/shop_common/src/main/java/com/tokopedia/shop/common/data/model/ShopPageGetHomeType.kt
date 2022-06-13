package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPageGetHomeType(
        @SerializedName("shopHomeType")
        @Expose
        val shopHomeType: String = "",

        @SerializedName("homeLayoutData")
        @Expose
        val homeLayoutData: HomeLayoutData = HomeLayoutData(),

        @SerializedName("shopLayoutFeatures")
        @Expose
        val shopLayoutFeatures: List<ShopLayoutFeatures> = listOf()
) {

    data class Response(
            @SerializedName("shopPageGetHomeType")
            val shopPageGetHomeType: ShopPageGetHomeType = ShopPageGetHomeType()
    )

    data class HomeLayoutData(
            @SerializedName("layoutID")
            @Expose
            val layoutId: String = "",

            @SerializedName("masterLayoutID")
            @Expose
            val masterLayoutId: String = "",

            @SerializedName("widgetIDList")
            @Expose
            val widgetIdList: List<WidgetIdList> = listOf(),
    ) {
        data class WidgetIdList(
                @SerializedName("widgetID")
                @Expose
                val widgetId: String = "",

                @SerializedName("widgetMasterID")
                @Expose
                val widgetMasterId: String = "",

                @SerializedName("widgetType")
                @Expose
                val widgetType: String = "",

                @SerializedName("widgetName")
                @Expose
                val widgetName: String = ""
        )
    }

    data class ShopLayoutFeatures(
        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false
    )
}