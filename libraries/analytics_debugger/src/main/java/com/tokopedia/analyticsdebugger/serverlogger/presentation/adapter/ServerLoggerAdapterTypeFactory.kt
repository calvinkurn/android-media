package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.LoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.LoggerUiModel

class ServerLoggerAdapterTypeFactory: BaseAdapterTypeFactory(), ServerLoggerTypeFactory {

    override fun type(loggerUiModel: List<LoggerUiModel>): Int {
        TODO("Not yet implemented")
    }

    override fun type(loggerPriorityUiModel: LoggerPriorityUiModel): Int {
        TODO("Not yet implemented")
    }

}