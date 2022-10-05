package com.tokopedia.sellerhomecommon.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.TableItemFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlWithIconViewHolder

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableItemAdapter(
    listener: TableColumnHtmlViewHolder.Listener,
    listenerHtmlWithIcon: TableColumnHtmlWithIconViewHolder.Listener
) : BaseAdapter<TableItemFactoryImpl>(TableItemFactoryImpl(listener,listenerHtmlWithIcon)) {

    val items: MutableList<Visitable<*>>
        get() = visitables
}