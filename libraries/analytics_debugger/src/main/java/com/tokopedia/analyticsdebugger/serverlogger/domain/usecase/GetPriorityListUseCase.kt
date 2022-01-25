package com.tokopedia.analyticsdebugger.serverlogger.domain.usecase

import com.tokopedia.analyticsdebugger.serverlogger.domain.mapper.ServerLoggerMapper
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.logger.repository.LoggerRepository
import javax.inject.Inject

class GetPriorityListUseCase @Inject constructor(
    private val loggerRepository: LoggerRepository?,
    private val serverLoggerMapper: ServerLoggerMapper
) {

    suspend fun execute(chipsSelected: String): ServerLoggerPriorityUiModel {
        return serverLoggerMapper.mapToPriorityList(
            loggerRepository?.getPriorityList() ?: emptyList(), chipsSelected
        )
    }
}