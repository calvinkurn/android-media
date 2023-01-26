package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.PageSource
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class DeleteAllWebSocketLogUseCase @Inject constructor(
    private val playWebSocketLogRepository: PlayWebSocketLogRepository,
    private val topchatWebSocketLogRepository: TopchatWebSocketLogRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<PageSource, Unit>(dispatchers.io) {

    override suspend fun execute(params: PageSource) {
        if (params == PageSource.PLAY) {
            playWebSocketLogRepository.deleteAll()
        } else if (params == PageSource.TOPCHAT) {
            topchatWebSocketLogRepository.deleteAll()
        }
    }

    override fun graphqlQuery() = "" // no-op
}
