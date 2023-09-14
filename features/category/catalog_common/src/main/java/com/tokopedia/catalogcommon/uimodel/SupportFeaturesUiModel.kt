package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class SupportFeaturesUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val titleSection: String,
    val items: List<ItemSupportFeaturesUiModel>
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemSupportFeaturesUiModel(
        val id: String,
        val icon: String,
        val title: String,
        val titleColor: Int,
        val descColor: Int,
        val description: String,
        val backgroundColor: Int,
    )

//    companion object {
//        private const val dummyDarkMode = true
//        fun dummyTopFeatures() = SupportFeaturesUiModel(
//            "dummy", "", "", "#FFFFFF".stringHexColorParseToInt(),
//            items = listOf(
//                ItemTopFeatureUiModel(
//                    "",
//                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
//                    "360 All Round",
//                    textColor = colorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
//                    backgroundColor = colorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
//                ),
//                ItemTopFeatureUiModel(
//                    "",
//                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
//                    "360 All Round",
//                    textColor = colorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
//                    backgroundColor = colorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
//                ),
//                ItemTopFeatureUiModel(
//                    "",
//                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
//                    "360 All Round",
//                    textColor = colorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
//                    backgroundColor = colorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
//                ),
//                ItemTopFeatureUiModel(
//                    "",
//                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
//                    "360 All Round",
//                    textColor = colorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
//                    backgroundColor = colorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
//                )
//            )
//        )
//    }
}
