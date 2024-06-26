package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableItemDivider
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlWithIconViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlWithMetaViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnImageViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnTextViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableHeaderColumnViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableItemDividerViewHolder

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableItemFactoryImpl(
    private val listener: TableColumnHtmlViewHolder.Listener,
    private val metaTableListener: TableColumnHtmlWithMetaViewHolder.Listener
    ) : BaseAdapterTypeFactory(), TableItemFactory {

    override fun type(header: TableHeaderUiModel): Int {
        return TableHeaderColumnViewHolder.RES_LAYOUT
    }

    override fun type(column: TableRowsUiModel.RowColumnText): Int {
        return TableColumnTextViewHolder.RES_LAYOUT
    }

    override fun type(column: TableRowsUiModel.RowColumnImage): Int {
        return TableColumnImageViewHolder.RES_LAYOUT
    }

    override fun type(column: TableRowsUiModel.RowColumnHtml): Int {
        return TableColumnHtmlViewHolder.RES_LAYOUT
    }

    override fun type(column: TableRowsUiModel.RowColumnHtmlWithIcon): Int {
        return TableColumnHtmlWithIconViewHolder.RES_LAYOUT
    }

    override fun type(column: TableRowsUiModel.RowColumnHtmlWithMeta): Int {
        return TableColumnHtmlWithMetaViewHolder.RES_LAYOUT
    }
    override fun type(divider: TableItemDivider): Int = TableItemDividerViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TableHeaderColumnViewHolder.RES_LAYOUT -> TableHeaderColumnViewHolder(parent)
            TableColumnTextViewHolder.RES_LAYOUT -> TableColumnTextViewHolder(parent)
            TableColumnImageViewHolder.RES_LAYOUT -> TableColumnImageViewHolder(parent)
            TableColumnHtmlViewHolder.RES_LAYOUT -> TableColumnHtmlViewHolder(parent, listener)
            TableColumnHtmlWithIconViewHolder.RES_LAYOUT -> TableColumnHtmlWithIconViewHolder(parent, listener)
            TableColumnHtmlWithMetaViewHolder.RES_LAYOUT -> TableColumnHtmlWithMetaViewHolder(parent, metaTableListener)
            TableItemDividerViewHolder.RES_LAYOUT -> TableItemDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
