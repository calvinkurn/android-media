package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableItemDivider
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel

/**
 * Created By @ilhamsuaib on 01/07/20
 */

interface TableItemFactory {

    fun type(header: TableHeaderUiModel): Int

    fun type(column: TableRowsUiModel.RowColumnText): Int

    fun type(column: TableRowsUiModel.RowColumnImage): Int

    fun type(column: TableRowsUiModel.RowColumnHtml): Int

    fun type(column: TableRowsUiModel.RowColumnHtmlWithIcon): Int

    fun type(divider: TableItemDivider): Int
}