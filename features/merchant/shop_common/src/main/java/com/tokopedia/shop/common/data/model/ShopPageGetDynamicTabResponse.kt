package com.tokopedia.shop.common.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ShopPageGetDynamicTabResponse(
    @SerializedName("shopPageGetDynamicTab")
    val shopPageGetDynamicTab: ShopPageGetDynamicTab = ShopPageGetDynamicTab()
) {

    data class ShopPageGetDynamicTab(
        @SerializedName("tabData")
        val tabData: List<TabData> = listOf(),
    ) {
        data class TabData(
            @SerializedName("name")
            var name: String = "",
            @SerializedName("isActive")
            var isActive: Int = 0,
            @SerializedName("isFocus")
            var isFocus: Int = 0,
            @SerializedName("isDefault")
            var isDefault: Boolean = false,
            @SerializedName("errorMessage")
            var errorMessage: String = "",
            @SerializedName("text")
            var text: String = "",
            @SerializedName("icon")
            var icon: String = "",
            @SerializedName("iconFocus")
            var iconFocus: String = "",
            @SerializedName("type")
            var type: String = "",
            @SerializedName("bgColors")
            @Expose
            var listBackgroundColor: List<String> = listOf(),
            @SerializedName("textColor")
            @Expose
            var textColor: String = "",
            @SerializedName("shopLayoutFeature")
            var shopLayoutFeature: List<ShopLayoutFeature> = arrayListOf(),
            @SerializedName("data")
            var data: Data = Data()
        ) {
            data class ShopLayoutFeature(
                @SerializedName("name")
                var name: String = "",
                @SerializedName("isActive")
                var isActive: Boolean = false
            )

            data class Data(
                @SerializedName("homeLayoutData")
                val homeLayoutData: HomeLayoutData = HomeLayoutData(),

                @SuppressLint("Invalid Data Type")
                @SerializedName("widgetIDList")
                @Expose
                val widgetIdList: List<WidgetIdList> = listOf()
            )
        }
    }
}