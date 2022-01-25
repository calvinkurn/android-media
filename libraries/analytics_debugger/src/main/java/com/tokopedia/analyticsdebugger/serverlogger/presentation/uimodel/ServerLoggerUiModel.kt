package com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel

import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerAdapterTypeFactory

data class ServerLoggerUiModel(
    val serverChannel: List<String> = emptyList(),
    val tag: String = "",
    val previewMessage: String = "",
    val message: String = "",
    val dateTime: String = "",
    val priority: String = ""
): BaseServerLoggerUiModel {
    override fun type(typeFactory: ServerLoggerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}