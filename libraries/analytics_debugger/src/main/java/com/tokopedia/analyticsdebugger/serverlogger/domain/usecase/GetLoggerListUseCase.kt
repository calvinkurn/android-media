package com.tokopedia.analyticsdebugger.serverlogger.domain.usecase

import com.tokopedia.analyticsdebugger.serverlogger.domain.mapper.ServerLoggerMapper
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel
import com.tokopedia.logger.repository.LoggerRepository
import javax.inject.Inject

class GetLoggerListUseCase @Inject constructor(
    private val loggerRepository: LoggerRepository?,
    private val serverLoggerMapper: ServerLoggerMapper
) {

    suspend fun execute(
        keyword: String,
        serverChannel: String,
        limit: Int,
        offSet: Int
    ): List<ItemServerLoggerUiModel> {
        return serverLoggerMapper.mapToLoggerListUiModel(
            loggerRepository?.getLoggerList(serverChannel, limit, offSet) ?: emptyList(),
            keyword
        )
    }

}