package com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel

data class ServerLoggerState(
    val data: List<BaseServerLoggerUiModel> = emptyList(),
    val isLoading: Boolean = false
)
