package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel

interface ServerLoggerListener : ItemPriorityServerLoggerListener, ItemServerLoggerListener

interface ItemPriorityServerLoggerListener {
    fun onChipsClicked(position: Int, chipsName: String)
}

interface ItemServerLoggerListener {
    fun onItemClicked(item: ItemServerLoggerUiModel)
}