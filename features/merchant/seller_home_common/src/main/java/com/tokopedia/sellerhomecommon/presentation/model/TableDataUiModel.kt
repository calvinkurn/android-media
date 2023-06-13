package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.TableItemFactory

/**
 * Created By @ilhamsuaib on 10/06/20
 */

data class TableDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val dataSet: List<TablePageUiModel> = emptyList()
) : BaseDataUiModel, LastUpdatedDataInterface {

    override fun isWidgetEmpty(): Boolean {
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
    open val width: Int = 0,
    open val meta: Meta = Meta()
) : Visitable<TableItemFactory> {

    data class RowColumnText(
        override val valueStr: String = "",
        override val width: Int = 0,
        override val meta: Meta = Meta(),
        val isLeftAlign: Boolean = false
    ) : TableRowsUiModel(valueStr, width, meta) {

        override fun type(typeFactory: TableItemFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class RowColumnImage(
        override val valueStr: String = "",
        override val width: Int = 0,
        override val meta: Meta = Meta(),
    ) : TableRowsUiModel(valueStr, width, meta) {

        override fun type(typeFactory: TableItemFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class RowColumnHtml(
        override val valueStr: String = "",
        override val width: Int = 0,
        override val meta: Meta = Meta(),
        val isLeftAlign: Boolean = false,
        var colorInt: Int? = null
    ) : TableRowsUiModel(valueStr, width, meta) {

        override fun type(typeFactory: TableItemFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class RowColumnHtmlWithIcon(
        override val valueStr: String = "",
        override val width: Int = 0,
        val icon: String = "",
        override val meta: Meta = Meta(),
        val isLeftAlign: Boolean = false,
        var colorInt: Int? = null
    ) : TableRowsUiModel(valueStr, width) {

        override fun type(typeFactory: TableItemFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class Meta(
        val flag: String = String.EMPTY
    )
}
