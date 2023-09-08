package com.tokopedia.shop.pageheader.presentation.uimodel

import com.tokopedia.shop.common.view.model.ShopPageColorSchema

data class ShopPageHeaderLayoutUiModel(
    val listConfig: List<Config> = listOf(),
    val isOverrideTheme: Boolean = false,
    //TODO need to move ShopPageHeaderWidgetUiModel to this model in the future
    //val listWidget: List<ShopPageHeaderWidgetUiModel> = listOf(),
) {

    enum class ConfigName(val value: String) {
        SHOP_HEADER("shop_header"),
        SHOP_BODY("shop_body"),
        SHOP_BOTTOM_NAVBAR("shop_navbar"),
    }

    enum class BgObjectType(val value: String) {
        IMAGE("image"),
        VIDEO("video"),
    }

    enum class ColorType(val value: String){
        LIGHT("light"),
        DARK("dark")
    }

    fun getShopConfigListByName(configName: ConfigName): Config? {
        return listConfig.firstOrNull { it.name == configName.value }
    }

    data class Config(
        val name: String = "",
        val type: String = "",
        val patternColorType: String = "",
        val listBackgroundColor: List<String> = listOf(),
        val listBackgroundObject: List<BackgroundObject> = listOf(),
        val colorSchema: ShopPageColorSchema = ShopPageColorSchema(),
    ) {

        fun getBackgroundObject(bgObjectType: BgObjectType): BackgroundObject? {
            return listBackgroundObject.firstOrNull { it.type.startsWith(bgObjectType.value) }
        }

        data class BackgroundObject(
            val url: String = "",
            val type: String = ""
        )
    }
}
