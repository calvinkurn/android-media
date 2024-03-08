package com.tokopedia.catalogcommon.uimodel

import android.os.Parcelable
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.parcelize.Parcelize

data class ColumnedInfoUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val sectionTitle: String = "",
    val widgetContent: ColumnData = ColumnData(),
    val widgetContentThreeColumn: List<ColumnData> = listOf(),
    val fullContent: List<ColumnData> = emptyList(),
    val hasMoreData: Boolean = false,
    val columnType: String = ""
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {

    companion object {
        const val FALLBACK_COLUMN_TYPE = "title_value_on_2"
        const val COLUMN_TITLE_ON_3_COLUMN_TYPE = "column_title_on_3"
        const val CELL_TITLE_ON_3_COLUMN_TYPE = "cell_title_on_3"
        const val VALUE_ON_2_COLUMN_TYPE = "value_on_2"
    }

    @Parcelize
    data class ColumnData(
        val title: String = "",
        val rowData: List<Pair<String, String>> = emptyList(),
        val rowColor: Pair<Int, Int> = Pair(Int.ZERO, Int.ZERO) // pair of title color and value color
    ) : Parcelable

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
