package com.tokopedia.analyticsdebugger.serverlogger.domain.usecase

import com.tokopedia.logger.repository.LoggerRepository
import javax.inject.Inject

class GetLoggerListUseCase @Inject constructor(
    private val loggerRepository: LoggerRepository
) {
}