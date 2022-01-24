package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.LoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.LoggerUiModel

interface ServerLoggerTypeFactory {
    fun type(loggerUiModel: List<LoggerUiModel>): Int
    fun type(loggerPriorityUiModel: LoggerPriorityUiModel): Int
}