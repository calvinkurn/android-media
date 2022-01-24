package com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel

data class LoggerUiModel(
    val serverChannel: List<String> = emptyList(),
    val tag: String = "",
    val previewMessage: String = "",
    val message: String = "",
    val dateTime: String = "",
    val priority: String = ""
)