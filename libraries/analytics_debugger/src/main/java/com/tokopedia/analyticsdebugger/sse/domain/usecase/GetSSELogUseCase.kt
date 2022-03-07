package com.tokopedia.analyticsdebugger.sse.domain.usecase

import com.tokopedia.analyticsdebugger.sse.domain.repository.SSELogRepository
import com.tokopedia.analyticsdebugger.sse.ui.mapper.SSELogMapper
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class GetSSELogUseCase @Inject constructor(
    private val sseLogRepository: SSELogRepository,
    private val sseLogMapper: SSELogMapper
): UseCase<List<SSELogUiModel>>() {

    private var query: String = ""

    fun setParam(query: String) {
        this.query = "%$query%"
    }

    override suspend fun executeOnBackground(): List<SSELogUiModel> {
        val response = sseLogRepository.get(query)
        return sseLogMapper.mapEntityToUiModel(response)
    }
}