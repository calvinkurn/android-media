package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class ColumnedInfoUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val sectionTitle: String = "",
    val widgetContent: List<Pair<String, String>> = emptyList(),
    val fullContent: List<ColumnData> = emptyList(),
    val hasMoreData: Boolean = false
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {
    data class ColumnData (
        val title: String = "",
        val rowData: List<Pair<String, String>> = emptyList()
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
