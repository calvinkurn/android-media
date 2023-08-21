package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.SerializedName

data class ShopPageHeaderLayoutResponse(
    @SerializedName("ShopPageGetHeaderLayout")
    val shopPageGetHeaderLayout: ShopPageGetHeaderLayout = ShopPageGetHeaderLayout()
) {
    data class ShopPageGetHeaderLayout(
        @SerializedName("generalComponentConfigList")
        val generalComponentConfigList: List<GeneralComponentConfigList> = listOf(),
        @SerializedName("isOverrideTheme")
        val isOverrideTheme: Boolean = false,
        @SerializedName("widgets")
        val widgets: List<Widget> = listOf(),
    ) {
        data class GeneralComponentConfigList(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("data")
            val data: DataComponentConfig = DataComponentConfig(),
        ){
            data class DataComponentConfig(
                @SerializedName("patternColorType")
                val patternColorType: String = "",
                @SerializedName("bgColors")
                val listBackgroundColor: List<String> = listOf(),
                @SerializedName("bgObjects")
                val listBackgroundObject: List<BackgroundObject> = listOf(),
                @SerializedName("colorSchemaList")
                val listColorSchema: List<ColorSchema> = listOf(),
            ){
                data class BackgroundObject(
                    @SerializedName("url")
                    val url: String = "",
                    @SerializedName("type")
                    val type: String = ""
                )

                data class ColorSchema(
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("type")
                    val type: String = "",
                    @SerializedName("value")
                    val value: String = ""
                )
            }
        }
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
                    val label: String = "",
                    @SerializedName("images")
                    val images: Images = Images(),
                    @SerializedName("textComponent")
                    val textComponent: TextComponent = TextComponent()
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

                data class Images(
                    @SerializedName("data")
                    val data: List<Data> = listOf(),
                    @SerializedName("style")
                    val style: Int = -1
                ) {
                    data class Data(
                        @SerializedName("image")
                        val image: String = "",
                        @SerializedName("imageLink")
                        val imageLink: String = "",
                        @SerializedName("isBottomSheet")
                        val isBottomSheet: Boolean = false
                    )
                }

                data class TextComponent(
                    @SerializedName("data")
                    val data: Data = Data(),
                    @SerializedName("style")
                    val style: Int = -1
                ) {
                    data class Data(
                        @SerializedName("icon")
                        val icon: String = "",
                        @SerializedName("isBottomSheet")
                        val isBottomSheet: Boolean = false,
                        @SerializedName("textHtml")
                        val textHtml: String = "",
                        @SerializedName("textLink")
                        val textLink: String = ""
                    )
                }
            }
        }
    }
}
