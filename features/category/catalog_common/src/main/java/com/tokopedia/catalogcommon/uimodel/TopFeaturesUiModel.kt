package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.catalogcommon.util.textColorMapping

data class TopFeaturesUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val items: List<ItemTopFeatureUiModel>
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

    data class ItemTopFeatureUiModel(
        val id: String,
        val icon: String,
        val name: String,
        val backgroundColor: Int,
        val textColor: Int
    )

    companion object {
        private const val dummyDarkMode = false
        fun dummyTopFeatures() = TopFeaturesUiModel(
            "dummy", "", "", "#FFFFFF".stringHexColorParseToInt(),
            items = listOf(
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "360 All Round",
                    textColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    backgroundColor = textColorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
                ),
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "360 All Round",
                    textColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    backgroundColor = textColorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
                ),
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "360 All Round",
                    textColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    backgroundColor = textColorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
                ),
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "360 All Round",
                    textColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    backgroundColor = textColorMapping(dummyDarkMode, "#AAB4C8", "#FFFFFF", 20),
                )
            )
        )
    }
}
