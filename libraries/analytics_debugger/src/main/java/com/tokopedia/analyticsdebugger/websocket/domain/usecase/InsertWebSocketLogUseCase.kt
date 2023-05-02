package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.param.InsertWebSocketLogParam
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class InsertWebSocketLogUseCase @Inject constructor(
    private val playWebSocketLogRepository: PlayWebSocketLogRepository,
    private val topchatWebSocketLogRepository: TopchatWebSocketLogRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<InsertWebSocketLogParam, Unit>(dispatchers.io) {

    override suspend fun execute(params: InsertWebSocketLogParam) {
        when(params.pageSource) {
            WebSocketLogPageSource.PLAY -> playInsertWebSocketLog(params)
            WebSocketLogPageSource.TOPCHAT -> topchatInsertWebSocketLog(params)
            else -> Unit
        }
    }

    private suspend fun playInsertWebSocketLog(param: InsertWebSocketLogParam) {
        if (param.info.play == null) return

        playWebSocketLogRepository.insert(
            PlayWebSocketLogEntity(
                source = param.info.play.source,
                channelId = param.info.play.channelId,
                gcToken = param.info.play.gcToken,
                event = param.info.event,
                message = param.info.message,
                timestamp = System.currentTimeMillis(),
                warehouseId = param.info.play.warehouseId,
            )
        )
    }

    private suspend fun topchatInsertWebSocketLog(param: InsertWebSocketLogParam) {
        if (param.info.topchat == null) return

        topchatWebSocketLogRepository.insert(
            TopchatWebSocketLogEntity(
                source = param.info.topchat.source,
                code = param.info.topchat.code,
                messageId = param.info.topchat.messageId,
                header = param.info.topchat.header,
                event = param.info.event,
                message = param.info.message,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override fun graphqlQuery() = "" // No-op
}
