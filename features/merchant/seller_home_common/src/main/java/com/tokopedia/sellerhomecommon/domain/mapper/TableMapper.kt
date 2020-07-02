package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.HeaderModel
import com.tokopedia.sellerhomecommon.domain.model.TableDataModel
import com.tokopedia.sellerhomecommon.domain.model.TableDataSetModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class TableMapper @Inject constructor() {

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

    fun mapRemoteModelToUiModel(tableData: List<TableDataModel>): List<TableDataUiModel> {
        return tableData.map {
            TableDataUiModel(
                    dataKey = it.dataKey,
                    error = it.errorMsg,
                    dataSet = getTableDataSet(it.data)
            )
        }
    }

    private fun getTableDataSet(data: TableDataSetModel): List<TablePageUiModel> {
        val headers: List<TableHeaderUiModel> = getHeaders(data.headers)
        val tablePages = mutableListOf<TablePageUiModel>()

        var rows = mutableListOf<TableRowsUiModel>()
        val rowCount = data.rows.size
        data.rows.forEachIndexed { i, row ->
            row.columns.forEachIndexed { j, col ->
                if (j < headers.size) {
                    val width = headers[j].width
                    val rowColumn: TableRowsUiModel = when (col.type) {
                        COLUMN_TEXT -> TableRowsUiModel.RowColumnText(col.value, width)
                        COLUMN_IMAGE -> TableRowsUiModel.RowColumnImage(col.value, width)
                        else -> TableRowsUiModel.RowColumnHtml(col.value, width) //it's COLUMN_HTML
                    }
                    rows.add(rowColumn)
                }
            }

            if (i.plus(1) % MAX_ROWS_PER_PAGE == 0 && rowCount >= MAX_ROWS_PER_PAGE) {
                val tablePage = TablePageUiModel(headers, rows)
                tablePages.add(tablePage)
                rows = mutableListOf()
            } else if (i == rowCount.minus(1)) {
                val tablePage = TablePageUiModel(headers, rows)
                tablePages.add(tablePage)
            }
        }

        return tablePages.reversed()
    }

    private fun getHeaders(headers: List<HeaderModel>): List<TableHeaderUiModel> {
        return headers.map {
            val headerWidth = if (it.width < 0) 0 else it.width
            return@map TableHeaderUiModel(it.title, headerWidth)
        }
    }
}