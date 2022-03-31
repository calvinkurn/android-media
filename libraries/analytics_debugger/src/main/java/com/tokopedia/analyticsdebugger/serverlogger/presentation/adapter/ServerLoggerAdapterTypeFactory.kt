package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.viewholder.ItemServerLoggerListViewHolder
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.viewholder.ItemServerLoggerPriorityViewHolder
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel

class ServerLoggerAdapterTypeFactory(
    private val serverLoggerListener: ServerLoggerListener
) : BaseAdapterTypeFactory(), ServerLoggerTypeFactory {

    override fun type(loggerPriorityUiModel: ServerLoggerPriorityUiModel): Int {
        return ItemServerLoggerPriorityViewHolder.LAYOUT
    }

    override fun type(loggerUiModelItem: ItemServerLoggerUiModel): Int {
        return ItemServerLoggerListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ItemServerLoggerListViewHolder.LAYOUT -> ItemServerLoggerListViewHolder(
                parent,
                serverLoggerListener
            )
            ItemServerLoggerPriorityViewHolder.LAYOUT -> ItemServerLoggerPriorityViewHolder(
                parent,
                serverLoggerListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}