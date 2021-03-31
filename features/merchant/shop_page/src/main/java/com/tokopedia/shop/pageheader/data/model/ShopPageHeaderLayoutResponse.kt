package com.tokopedia.shop.pageheader.data.model


import com.google.gson.annotations.SerializedName

data class ShopPageHeaderLayoutResponse(
        @SerializedName("ShopPageGetHeaderLayout")
        val shopPageGetHeaderLayout: ShopPageGetHeaderLayout = ShopPageGetHeaderLayout()
) {
    data class ShopPageGetHeaderLayout(
            @SerializedName("widgets")
            val widgets: List<Widget> = listOf()
    ) {
        data class Widget(
                @SerializedName("component")
                val listComponent: List<Component> = listOf(),
                @SerializedName("name")
                val name: String = "",
                @SerializedName("type")
                val type: String = "",
                @SerializedName("widgetID")
                val widgetID: String = ""
        ) {
            data class Component(
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("type")
                    val type: String = "",
                    @SerializedName("data")
                    val data: Data = Data()
            ) {
                data class Data(
                        @SerializedName("__typename")
                        val __typeName: String = "",
                        @SerializedName("ctaLink")
                        val ctaLink: String = "",
                        @SerializedName("ctaText")
                        val ctaText: String = "",
                        @SerializedName("ctaIcon")
                        val ctaIcon: String = "",
                        @SerializedName("icon")
                        val icon: String = "",
                        @SerializedName("image")
                        val image: String = "",
                        @SerializedName("imageLink")
                        val imageLink: String = "",
                        @SerializedName("isBottomSheet")
                        val isBottomSheet: Boolean = false,
                        @SerializedName("link")
                        val link: String = "",
                        @SerializedName("text")
                        val listText: List<Text> = listOf(),
                        @SerializedName("buttonType")
                        val buttonType: String = "",
                        @SerializedName("label")
                        val label: String = ""
                )

                data class Text(
                        @SerializedName("icon")
                        val icon: String = "",
                        @SerializedName("textLink")
                        val textLink: String = "",
                        @SerializedName("textHtml")
                        val textHtml: String = "",
                        @SerializedName("isBottomSheet")
                        val isBottomSheet: Boolean = false
                )
            }
        }
    }
}