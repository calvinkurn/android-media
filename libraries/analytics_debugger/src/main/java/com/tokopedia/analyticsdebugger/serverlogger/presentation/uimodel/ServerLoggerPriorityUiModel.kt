package com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel

import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerAdapterTypeFactory

data class ServerLoggerPriorityUiModel(
    val priority: List<ItemPriorityUiModel> = emptyList()
) : BaseServerLoggerUiModel {
    override fun type(typeFactory: ServerLoggerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}