package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel

interface ServerLoggerTypeFactory {
    fun type(loggerPriorityUiModel: ServerLoggerPriorityUiModel): Int
    fun type(loggerUiModelItem: ItemServerLoggerUiModel): Int
}