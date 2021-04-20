package com.tokopedia.sellerhomecommon.domain.mapper

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
                        else -> TableRowsUiModel.RowColumnHtml(col.value, width, firstTextColumn == col) //it's COLUMN_HTML
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
}