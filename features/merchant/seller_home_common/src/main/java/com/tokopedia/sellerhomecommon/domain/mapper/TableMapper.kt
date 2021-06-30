package com.tokopedia.sellerhomecommon.domain.mapper

import android.graphics.Color
import com.tokopedia.sellerhomecommon.domain.model.GetTableDataResponse
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.domain.model.HeaderModel
import com.tokopedia.sellerhomecommon.domain.model.TableDataSetModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class TableMapper @Inject constructor(): BaseResponseMapper<GetTableDataResponse, List<TableDataUiModel>> {

    companion object {
        /**
         * this constants can be seen on https://tokopedia.atlassian.net/wiki/spaces/~354932339/pages/719618522/Version+3
         * in simple table section
         * */
        private const val COLUMN_TEXT = 1
        private const val COLUMN_IMAGE = 2
        private const val COLUMN_HTML = 4

        private const val MAX_ROWS_PER_PAGE = 5

        private const val COLOR = "color"
        private const val BACKGROUND_COLOR = "background-color"
        private const val APOSTROPHE = "\""
    }

    override fun mapRemoteDataToUiData(response: GetTableDataResponse, isFromCache: Boolean): List<TableDataUiModel> {
        return response.fetchSearchTableWidgetData.data.map {
            TableDataUiModel(
                    dataKey = it.dataKey,
                    error = it.errorMsg,
                    dataSet = getTableDataSet(it.data),
                    isFromCache = isFromCache,
                    showWidget = it.showWidget.orFalse()
            )
        }
    }

    private fun getTableDataSet(data: TableDataSetModel): List<TablePageUiModel> {
        val headers: List<TableHeaderUiModel> = getHeaders(data.headers)
        val tablePages = mutableListOf<TablePageUiModel>()

        var rows = mutableListOf<TableRowsUiModel>()
        val rowCount = data.rows.size
        data.rows.forEachIndexed { i, row ->
            val firstTextColumn = row.columns.firstOrNull { it.type == COLUMN_TEXT || it.type == COLUMN_HTML }
            row.columns.forEachIndexed { j, col ->
                if (j < headers.size) {
                    val width = headers[j].width
                    val rowColumn: TableRowsUiModel = when (col.type) {
                        COLUMN_TEXT -> TableRowsUiModel.RowColumnText(col.value, width, firstTextColumn == col)
                        COLUMN_IMAGE -> TableRowsUiModel.RowColumnImage(col.value, width)
                        else -> TableRowsUiModel.RowColumnHtml(col.value, width, firstTextColumn == col, getColorFromHtml(col.value)) //it's COLUMN_HTML
                    }
                    rows.add(rowColumn)
                }
            }

            if (i.plus(1).rem(MAX_ROWS_PER_PAGE) == 0 && rowCount >= MAX_ROWS_PER_PAGE) {
                val tablePage = TablePageUiModel(headers, rows)
                tablePages.add(tablePage)
                rows = mutableListOf()
            } else if (i == rowCount.minus(1)) {
                val tablePage = TablePageUiModel(headers, rows)
                tablePages.add(tablePage)
            }
        }

        return tablePages
    }

    private fun getHeaders(headers: List<HeaderModel>): List<TableHeaderUiModel> {
        val firstHeader = headers.firstOrNull { it.title.isNotBlank() }
        return headers.map { header ->
            val headerWidth = if (header.width < 0) 0 else header.width
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
        return try {
            // Example: <font color = "red">Example Text</font>
            val colorFromFontTagRegex = "<(.*)font(.+)color*=*(.+)".toRegex()
            // Example: <span style=color:#the_hex_color;><b>Habis</b></span>
            val colorFromStyleTagRegex = "(<+)(.+)style*=*(\"*)(.+)color*:*(.+)".toRegex()

            val colorString =
                    when {
                        htmlString.matches(colorFromFontTagRegex) -> getColorFromFontTag(htmlString)
                        htmlString.matches(colorFromStyleTagRegex) -> getColorFromStyleAttribute(htmlString)
                        else -> null
                    }

            if (colorString.isNullOrEmpty()) {
                null
            } else {
                Color.parseColor(colorString)
            }
        } catch (ex: Exception) {
            null
        }
    }

    private fun getColorFromFontTag(htmlString: String): String {
        // If font tag was used, get the value by delimiting between the apostrophes of color value
        val colorFromFont = htmlString.substringAfter(COLOR)
        val indexOfFirstApostrophe = colorFromFont.indexOf(APOSTROPHE)
        val indexOfSecondApostrophe = colorFromFont.indexOf(APOSTROPHE, indexOfFirstApostrophe + 1)
        return colorFromFont.substring(indexOfFirstApostrophe + 1, indexOfSecondApostrophe)
    }

    private fun getColorFromStyleAttribute(htmlString: String): String {
        // We remove background-color style attribute to be able to substring the color of the text only
        val colorWithoutBackgroundColor = htmlString.replace(BACKGROUND_COLOR, "")
        return colorWithoutBackgroundColor.substringAfter(COLOR)
                .substringBefore("\"").substringBefore(";")
                .replace("[^A-Za-z0-9#]+".toRegex(), "")
    }

}