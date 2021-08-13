package com.tokopedia.sellerhomecommon.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.TableItemFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlViewHolder

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableItemAdapter(listener: TableColumnHtmlViewHolder.Listener)
    : BaseAdapter<TableItemFactoryImpl>(TableItemFactoryImpl(listener)) {

    val items: MutableList<Visitable<*>>
        get() = visitables
}