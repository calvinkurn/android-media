package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.catalogcommon.util.colorMapping

data class CharacteristicUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    val items: List<ItemCharacteristicUiModel>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemCharacteristicUiModel(
        val id: String,
        val icon: String,
        val title: String,
        val textColorTitle: Int,
    )

    companion object {

        private const val dummyDarkMode = true
        fun dummyCharacteristicUiModel() = CharacteristicUiModel(
            "dummy", "", "", "#000000".stringHexColorParseToInt(),
            items = listOf(
                ItemCharacteristicUiModel(
                    "",
                    "",
                    "Lorep Ipsum",
                    textColorTitle = colorMapping(dummyDarkMode,"#F5F6FF", "#212121"),
                ),
                ItemCharacteristicUiModel(
                    "",
                    "",
                    "Lorep Ipsum",
                    textColorTitle = colorMapping(dummyDarkMode,"#F5F6FF", "#212121"),
                ),
                ItemCharacteristicUiModel(
                    "",
                    "",
                    "Lorep Ipsum",
                    textColorTitle = colorMapping(dummyDarkMode,"#F5F6FF", "#212121"),
                ),
            )
        )
    }
}
