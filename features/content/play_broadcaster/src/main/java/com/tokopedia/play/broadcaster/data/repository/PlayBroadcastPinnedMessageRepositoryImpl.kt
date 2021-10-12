package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastPinnedMessageRepository
import com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage.GetPinnedMessagesUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 12/10/21
 */
class PlayBroadcastPinnedMessageRepositoryImpl @Inject constructor(
    private val getPinnedMessagesUseCase: GetPinnedMessagesUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: PlayBroadcastMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayBroadcastPinnedMessageRepository {

    override suspend fun getActivePinnedMessage(
        channelId: String
    ): PinnedMessageUiModel? = withContext(dispatchers.io) {
//        val pinnedMessages = getPinnedMessagesUseCase.apply {
//            setRequestParams(GetPinnedMessagesUseCase.createParams(channelId))
//        }.executeOnBackground()
//
//        return@withContext mapper.mapPinnedMessage(pinnedMessages.data).firstOrNull {
//            it.isActive
//        }
        PinnedMessageUiModel(
            id = "1",
            message = "Halo teman2",
            isActive = true,
        )
    }

    override suspend fun setActivePinnedMessage(id: String?, channelId: String) {
        TODO("Not yet implemented")
    }
}