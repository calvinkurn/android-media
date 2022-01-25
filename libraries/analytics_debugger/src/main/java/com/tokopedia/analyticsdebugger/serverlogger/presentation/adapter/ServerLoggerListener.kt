package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

interface ServerLoggerListener: ItemPriorityServerLoggerListener

interface ItemPriorityServerLoggerListener {
    fun onChipsClicked(position: Int, chipsName: String)
}