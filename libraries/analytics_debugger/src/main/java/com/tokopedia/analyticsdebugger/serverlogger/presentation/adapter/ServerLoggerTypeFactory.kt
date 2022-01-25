package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerUiModel

interface ServerLoggerTypeFactory {
    fun type(loggerPriorityUiModel: ServerLoggerPriorityUiModel): Int
    fun type(loggerUiModel: ServerLoggerUiModel): Int
}