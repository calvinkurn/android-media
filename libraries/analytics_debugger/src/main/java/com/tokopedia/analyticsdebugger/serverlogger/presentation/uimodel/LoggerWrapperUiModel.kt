package com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel

data class LoggerWrapperUiModel(
    val loggerUiModel: List<LoggerUiModel> = emptyList(),
    val loggerPriority: LoggerPriorityUiModel = LoggerPriorityUiModel()
)