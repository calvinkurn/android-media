package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt

data class TopFeaturesUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? = null,
    override val widgetTextColor: Int? = null,
    override val darkMode: Boolean = false,
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
        val textColor: Int,
        val borderColor: Int
    )

    companion object {
        fun dummyTopFeatures() = TopFeaturesUiModel(
            "dummy", "", "", "#000000".stringHexColorParseToInt(),
            items = listOf(
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "Lorep Ipsum",
                    textColor = "#AEB2BF".stringHexColorParseToInt(),
                    backgroundColor = "#AAB4C8".stringHexColorParseToInt(30),
                    borderColor = "#AAB4C8".stringHexColorParseToInt(30)
                ),
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "Lorep Ipsum",
                    textColor = "#AEB2BF".stringHexColorParseToInt(),
                    backgroundColor = "#AAB4C8".stringHexColorParseToInt(30),
                    borderColor = "#AAB4C8".stringHexColorParseToInt(30)
                ),
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "Lorep Ipsum",
                    textColor = "#AEB2BF".stringHexColorParseToInt(),
                    backgroundColor = "#AAB4C8".stringHexColorParseToInt(30),
                    borderColor = "#AAB4C8".stringHexColorParseToInt(30)
                ),
                ItemTopFeatureUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "Lorep Ipsum",
                    textColor = "#AEB2BF".stringHexColorParseToInt(),
                    backgroundColor = "#AAB4C8".stringHexColorParseToInt(30),
                    borderColor = "#AAB4C8".stringHexColorParseToInt(30)
                )
            )
        )
    }
}
