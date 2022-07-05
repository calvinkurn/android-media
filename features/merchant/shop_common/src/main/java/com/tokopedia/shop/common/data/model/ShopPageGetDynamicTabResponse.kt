package com.tokopedia.shop.common.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPageGetDynamicTabResponse(
    @SerializedName("shopPageGetDynamicTab")
    val shopPageGetDynamicTab: ShopPageGetDynamicTab = ShopPageGetDynamicTab()
) {

    data class ShopPageGetDynamicTab(
        @SerializedName("tabData")
        @Expose
        val tabData: List<TabData> = listOf(),
    ) {
        data class TabData(
            @SerializedName("name")
            @Expose
            var name: String = "",
            @SerializedName("isActive")
            @Expose
            var isActive: Int = 0,
            @SerializedName("isFocus")
            @Expose
            var isFocus: Int = 0,
            @SerializedName("isDefault")
            @Expose
            var isDefault: Boolean = false,
            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = "",
            @SerializedName("text")
            @Expose
            var text: String = "",
            @SerializedName("icon")
            @Expose
            var icon: String = "",
            @SerializedName("iconFocus")
            @Expose
            var iconFocus: String = "",
            @SerializedName("type")
            @Expose
            var type: String = "",
            @SerializedName("bgColors")
            @Expose
            var listBackgroundColor: List<String> = listOf(),
            @SerializedName("textColor")
            @Expose
            var textColor: String = "",
            @SerializedName("shopLayoutFeature")
            @Expose
            var shopLayoutFeature: List<ShopLayoutFeature> = arrayListOf(),
            @SerializedName("data")
            @Expose
            var data: Data = Data()
        ) {
            data class ShopLayoutFeature(
                @SerializedName("name")
                @Expose
                var name: String = "",
                @SerializedName("isActive")
                @Expose
                var isActive: Boolean = false
            )

            data class Data(
                @SerializedName("homeLayoutData")
                @Expose
                val homeLayoutData: HomeLayoutData = HomeLayoutData(),

                @SuppressLint("Invalid Data Type")
                @SerializedName("widgetIDList")
                @Expose
                val widgetIdList: List<WidgetIdList> = listOf()
            )
        }
    }
}