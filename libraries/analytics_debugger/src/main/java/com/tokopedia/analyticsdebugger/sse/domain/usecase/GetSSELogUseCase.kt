package com.tokopedia.analyticsdebugger.sse.domain.usecase

import com.tokopedia.analyticsdebugger.sse.domain.`interface`.SSELogRepository
import com.tokopedia.analyticsdebugger.sse.ui.mapper.SSELogMapper
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class GetSSELogUseCaseImpl @Inject constructor(
    private val sseLogRepository: SSELogRepository,
    private val sseLogMapper: SSELogMapper
): UseCase<List<SSELogUiModel>>() {

    private var query: String = ""


    override suspend fun executeOnBackground(): List<SSELogUiModel> {
        return sseLogMapper.mapEntityToUiModel(sseLogRepository.get(query))
    }

    companion object {
        fun setParam(query: String) {
            query
        }
    }
}