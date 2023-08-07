package com.tokopedia.sellerhomecommon.domain.mapper

import android.graphics.Color
import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerhomecommon.common.DarkModeHelper
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetTableDataResponse
import com.tokopedia.sellerhomecommon.domain.model.HeaderModel
import com.tokopedia.sellerhomecommon.domain.model.TableDataSetModel
import com.tokopedia.sellerhomecommon.domain.model.TableRowMeta
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.utils.view.DarkModeUtil
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class TableMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean,
    private val darkModeHelper: DarkModeHelper
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetTableDataResponse, List<TableDataUiModel>> {

    companion object {
        /**
         * this constants can be seen on https://tokopedia.atlassian.net/wiki/spaces/~354932339/pages/719618522/Version+3
         * in simple table section
         * */
        private const val COLUMN_TEXT = 1
        private const val COLUMN_IMAGE = 2
        private const val COLUMN_HTML = 4
        private const val COLUMN_HTML_WITH_ICON = 8

        private const val MAX_ROWS_PER_PAGE = 5
    }

    private var dataKeys: List<DataKeyModel> = emptyList()

    override fun mapRemoteDataToUiData(
        response: GetTableDataResponse,
        isFromCache: Boolean,
    ): List<TableDataUiModel> {
        return response.fetchSearchTableWidgetData.data.mapIndexed { i, table ->
            var maxDisplay = dataKeys.getOrNull(i)?.maxDisplay ?: MAX_ROWS_PER_PAGE
            maxDisplay = if (maxDisplay == Int.ZERO) {
                MAX_ROWS_PER_PAGE
            } else {
                maxDisplay
            }

            return@mapIndexed TableDataUiModel(
                dataKey = table.dataKey,
                error = table.errorMsg,
                dataSet = getTableDataSet(table.data, maxDisplay),
                isFromCache = isFromCache,
                showWidget = table.showWidget.orFalse(),
                lastUpdated = getLastUpdatedMillis(table.dataKey, isFromCache)
            )
        }
    }

    fun setDataKeys(dataKeys: List<DataKeyModel>) {
        this.dataKeys = dataKeys
    }

    private fun getTableDataSet(
        data: TableDataSetModel,
        maxRowsPerPage: Int
    ): List<TablePageUiModel> {
        val headers: List<TableHeaderUiModel> = getHeaders(data.headers)
        val tablePages = mutableListOf<TablePageUiModel>()

        val tableRows = data.rows
        var rows = mutableListOf<TableRowsUiModel>()
        val rowCount = tableRows.size

        val zeroRowCount = Int.ZERO
        val oneRowCount = Int.ONE
        tableRows.forEachIndexed { i, row ->
            val firstTextColumn = row.columns.firstOrNull {
                it.type == COLUMN_TEXT || it.type == COLUMN_HTML || it.type == COLUMN_HTML_WITH_ICON
            }
            row.columns.forEachIndexed { j, col ->
                if (j < headers.size) {
                    val width = headers[j].width
                    val valueStr = darkModeHelper.makeHtmlDarkModeSupport(col.value)
                    val rowColumn: TableRowsUiModel = when (col.type) {
                        COLUMN_TEXT -> {
                            TableRowsUiModel.RowColumnText(
                                valueStr = valueStr,
                                width = width,
                                meta = getTableRowMeta(col.meta),
                                isLeftAlign = firstTextColumn == col
                            )
                        }
                        COLUMN_IMAGE -> TableRowsUiModel.RowColumnImage(col.value, width)
                        COLUMN_HTML -> {
                            TableRowsUiModel.RowColumnHtml(
                                valueStr = valueStr,
                                width = width,
                                meta = getTableRowMeta(col.meta),
                                isLeftAlign = firstTextColumn == col,
                                colorInt = getColorFromHtml(valueStr)
                            )
                        }
                        else -> {
                            TableRowsUiModel.RowColumnHtmlWithIcon(
                                valueStr = valueStr,
                                width = width,
                                icon = col.iconUrl.orEmpty(),
                                meta = getTableRowMeta(col.meta),
                                isLeftAlign = firstTextColumn == col,
                                colorInt = getColorFromHtml(valueStr)
                            ) // it's COLUMN_HTML WITH ICON
                        }
                    }
                    rows.add(rowColumn)
                }
            }

            if (i.plus(oneRowCount)
                    .rem(maxRowsPerPage) == zeroRowCount && rowCount >= maxRowsPerPage
            ) {
                val tablePage = TablePageUiModel(headers, rows)
                tablePages.add(tablePage)
                rows = mutableListOf()
            } else if (i == rowCount.minus(oneRowCount)) {
                val tablePage = TablePageUiModel(headers, rows)
                tablePages.add(tablePage)
            }
        }

        return tablePages
    }

    private fun getTableRowMeta(meta: String): TableRowsUiModel.Meta {
        return try {
            val metaModel = Gson().fromJson(meta, TableRowMeta::class.java)
            TableRowsUiModel.Meta(flag = metaModel.flag)
        } catch (e: Exception) {
            TableRowsUiModel.Meta(flag = String.EMPTY)
        }
    }

    private fun getHeaders(headers: List<HeaderModel>): List<TableHeaderUiModel> {
        val firstHeader = headers.firstOrNull { it.title.isNotBlank() }
        val noWidth = Int.ZERO
        return headers.map { header ->
            val headerWidth = if (header.width < noWidth) noWidth else header.width
            return@map TableHeaderUiModel(header.title, headerWidth, header == firstHeader)
        }
    }

    /**
     * A dumb but feasible way to parse html formatted string and get the text color value.
     * Make sure that the html string passed should not contain full html documents
     *
     * @param   htmlString  Html formatted string
     * @return  color of the text from html string
     */
    private fun getColorFromHtml(htmlString: String): Int? {
        runCatching {
            val regex = DarkModeUtil.HEX_COLOR_REGEX.toRegex()
            val result = regex.find(htmlString)
            val hexColor = result?.groupValues?.getOrNull(Int.ONE)
            hexColor?.let {
                return Color.parseColor(hexColor)
            }
            return null
        }
        return null
    }
}
