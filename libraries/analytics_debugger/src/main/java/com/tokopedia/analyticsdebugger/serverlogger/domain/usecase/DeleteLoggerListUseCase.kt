package com.tokopedia.analyticsdebugger.serverlogger.domain.usecase

import com.tokopedia.logger.repository.LoggerRepository
import javax.inject.Inject

class DeleteLoggerListUseCase @Inject constructor(
    private val loggerRepository: LoggerRepository?
) {
    suspend fun execute() {
        loggerRepository?.deleteAll()
    }
}