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
    @Parcelize
    data class ColumnData (
        val title: String = "",
        val rowData: List<Pair<String, String>> = emptyList(),
        val rowColor: Pair<Int, Int> = Pair(Int.ZERO, Int.ZERO),
        val rowIsBold: Pair<Boolean, Boolean>? = null
    ) : Parcelable

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
