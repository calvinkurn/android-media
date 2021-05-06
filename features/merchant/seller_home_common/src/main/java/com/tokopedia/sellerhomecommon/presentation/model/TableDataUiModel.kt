package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.TableItemFactory

/**
 * Created By @ilhamsuaib on 10/06/20
 */

data class TableDataUiModel(
        override var dataKey: String = "",
        override var error: String = "",
        val dataSet: List<TablePageUiModel> = emptyList(),
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return dataSet.all { it.rows.isEmpty() }
    }
}

data class TablePageUiModel(
        val headers: List<TableHeaderUiModel> = emptyList(),
        val rows: List<TableRowsUiModel> = emptyList(),
        val impressHolder: ImpressHolder = ImpressHolder()
)

object TableItemDivider : Visitable<TableItemFactory> {

    override fun type(typeFactory: TableItemFactory): Int {
        return typeFactory.type(this)
    }
}

data class TableHeaderUiModel(
        val title: String = "",
        val width: Int = 0,
        val isLeftAlign: Boolean = false
) : Visitable<TableItemFactory> {

    override fun type(typeFactory: TableItemFactory): Int {
        return typeFactory.type(this)
    }
}

sealed class TableRowsUiModel(
        open val valueStr: String = "",
        open val width: Int = 0
) : Visitable<TableItemFactory> {

    data class RowColumnText(
            override val valueStr: String = "",
            override val width: Int = 0,
            val isLeftAlign: Boolean = false
    ) : TableRowsUiModel(valueStr, width) {

        override fun type(typeFactory: TableItemFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class RowColumnImage(
            override val valueStr: String = "",
            override val width: Int = 0
    ) : TableRowsUiModel(valueStr, width) {

        override fun type(typeFactory: TableItemFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class RowColumnHtml(
            override val valueStr: String = "",
            override val width: Int = 0,
            val isLeftAlign: Boolean = false
    ) : TableRowsUiModel(valueStr, width) {

        override fun type(typeFactory: TableItemFactory): Int {
            return typeFactory.type(this)
        }
    }
}