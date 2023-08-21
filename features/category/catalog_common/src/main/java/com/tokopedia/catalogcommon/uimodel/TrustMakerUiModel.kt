package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt

data class TrustMakerUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? = null,
    override val widgetTextColor: Int? = null,
    val items: List<ItemTrustMakerUiModel>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemTrustMakerUiModel(
        val id: String,
        val icon: String,
        val title: String,
        val subTitle: String,
        val textColor: Int
    )

    companion object {
        fun dummyTrustMaker() = TrustMakerUiModel(
            "dummy", "", "", "#000000".stringHexColorParseToInt(),
            items = listOf(
                ItemTrustMakerUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "Lorep Ipsum",
                    subTitle = "Lorep Ipsum",
                    "#F5F6FF".stringHexColorParseToInt()
                ),
                ItemTrustMakerUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "Futuristik TV 2022",
                    subTitle = "IF Design Award",
                    "#F5F6FF".stringHexColorParseToInt()
                ),
                ItemTrustMakerUiModel(
                    "",
                    "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                    "Lorep Ipsum",
                    subTitle = "Lorep Ipsum",
                    "#F5F6FF".stringHexColorParseToInt()
                ),
            )
        )
    }
}
