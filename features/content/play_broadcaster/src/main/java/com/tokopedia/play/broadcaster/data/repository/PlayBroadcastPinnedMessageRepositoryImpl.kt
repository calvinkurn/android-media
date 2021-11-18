package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastPinnedMessageRepository
import com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage.AddPinnedMessageUseCase
import com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage.GetPinnedMessagesUseCase
import com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage.UpdatePinnedMessageUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 12/10/21
 */
class PlayBroadcastPinnedMessageRepositoryImpl @Inject constructor(
    private val getPinnedMessagesUseCase: GetPinnedMessagesUseCase,
    private val addPinnedMessageUseCase: AddPinnedMessageUseCase,
    private val updatePinnedMessageUseCase: UpdatePinnedMessageUseCase,
    private val mapper: PlayBroadcastMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayBroadcastPinnedMessageRepository {

    override suspend fun getActivePinnedMessage(
        channelId: String
    ): PinnedMessageUiModel? = withContext(dispatchers.io) {
        val pinnedMessages = getPinnedMessagesUseCase.apply {
            setRequestParams(GetPinnedMessagesUseCase.createParams(channelId))
        }.executeOnBackground()

        val pinnedList = mapper.mapPinnedMessage(pinnedMessages.data)

        return@withContext pinnedList.firstOrNull {
            it.isActive
        } ?: pinnedList.firstOrNull()
    }

    override suspend fun setPinnedMessage(
        id: String?,
        channelId: String,
        message: String
    ): PinnedMessageUiModel = withContext(dispatchers.io) {
        val pinnedId = id ?: addNewPinnedMessage(channelId, message).id
        updatePinnedMessage(pinnedId, channelId, message)
    }

    private suspend fun addNewPinnedMessage(
        channelId: String,
        message: String
    ) = withContext(dispatchers.io) {
        val response = addPinnedMessageUseCase.apply {
            setRequestParams(AddPinnedMessageUseCase.createParams(channelId, message))
        }.executeOnBackground()

        return@withContext PinnedMessageUiModel(
            id = response.data.messageIds.first(),
            message = message,
            isActive = false, //because add does not immediately activate the pinned
            editStatus = PinnedMessageEditStatus.Nothing,
        )
    }

    private suspend fun updatePinnedMessage(
        id: String,
        channelId: String,
        message: String
    ) = withContext(dispatchers.io) {
        val response = updatePinnedMessageUseCase.apply {
            setRequestParams(UpdatePinnedMessageUseCase.createParams(
                id = id.toLong(),
                channelId = channelId.toLong(),
                title = message,
                isActive = message.isNotBlank()
            ))
        }.executeOnBackground()

        return@withContext PinnedMessageUiModel(
            id = response.data.id,
            message = message,
            isActive = message.isNotBlank(),
            editStatus = PinnedMessageEditStatus.Nothing,
        )
    }
}