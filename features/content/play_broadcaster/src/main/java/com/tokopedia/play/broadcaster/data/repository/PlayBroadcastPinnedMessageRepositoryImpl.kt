package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastPinnedMessageRepository
import com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage.AddPinnedMessageUseCase
import com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage.GetPinnedMessagesUseCase
import com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage.UpdatePinnedMessageUseCase
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
    private val addPinnedMessageUseCase: AddPinnedMessageUseCase,
    private val updatePinnedMessageUseCase: UpdatePinnedMessageUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: PlayBroadcastMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayBroadcastPinnedMessageRepository {

    override suspend fun getActivePinnedMessage(
        channelId: String
    ): PinnedMessageUiModel? = withContext(dispatchers.io) {
        val pinnedMessages = getPinnedMessagesUseCase.apply {
            setRequestParams(GetPinnedMessagesUseCase.createParams(channelId))
        }.executeOnBackground()

        return@withContext mapper.mapPinnedMessage(pinnedMessages.data).firstOrNull {
            it.isActive
        }
//        PinnedMessageUiModel(
//            id = "1",
//            message = "Halo teman2",
//            isActive = true,
//        )
    }

    override suspend fun setActivePinnedMessage(
        id: String?,
        channelId: String,
        message: String
    ): String = withContext(dispatchers.io) {
        if (id == null) addNewPinnedMessage(channelId, message)
        else updatePinnedMessage(id, channelId, message)
    }

    private suspend fun addNewPinnedMessage(
        channelId: String,
        message: String
    ) = withContext(dispatchers.io) {
        val response = addPinnedMessageUseCase.apply {
            setRequestParams(AddPinnedMessageUseCase.createParams(channelId, message))
        }.executeOnBackground()

        return@withContext response.data.messageIds.first()
    }

    private suspend fun updatePinnedMessage(
        id: String,
        channelId: String,
        message: String
    ) = withContext(dispatchers.io) {
        val response = updatePinnedMessageUseCase.apply {
            setRequestParams(UpdatePinnedMessageUseCase.createParams(
                id.toLong(),
                channelId.toLong(),
                message
            ))
        }.executeOnBackground()

        return@withContext response.data.id
    }
}