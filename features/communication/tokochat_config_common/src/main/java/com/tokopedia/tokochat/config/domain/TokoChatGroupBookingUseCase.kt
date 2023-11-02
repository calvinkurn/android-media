package com.tokopedia.tokochat.config.domain

import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatCoroutineDispatchers
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.config.util.TokoChatServiceType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Predefined common usage for TokoChat related feature
 * Specific for initiation of channel (group booking)
 */
open class TokoChatGroupBookingUseCase@Inject constructor(
    private val repository: TokoChatRepository,
    dispatchers: TokoChatCoroutineDispatchers
) {

    private val _groupBookingResultFlow = MutableSharedFlow<TokoChatResult<String>>()
    val groupBookingResultFlow = _groupBookingResultFlow.asSharedFlow()

    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(dispatchers.main + supervisorJob)

    fun initGroupBookingChat(
        orderId: String,
        serviceType: TokoChatServiceType,
        orderChatType: OrderChatType = OrderChatType.Driver
    ) {
        val conversationRepository = repository.getConversationRepository()
        if (conversationRepository != null) {
            conversationRepository.initGroupBookingChat(
                orderId,
                serviceType.value,
                getConversationGroupBookingListener(),
                orderChatType
            )
        } else {
            _groupBookingResultFlow.tryEmit(
                TokoChatResult.Error(
                    MessageErrorException(CONVERSATION_NULL)
                )
            )
        }
    }

    private fun getConversationGroupBookingListener(): ConversationsGroupBookingListener {
        return object : ConversationsGroupBookingListener {
            override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
                scope.launch {
                    Timber.d(error)
                    _groupBookingResultFlow.emit(TokoChatResult.Error(error))
                }
            }
            override fun onGroupBookingChannelCreationStarted() {
                scope.launch {
                    _groupBookingResultFlow.emit(TokoChatResult.Loading)
                }
            }
            override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
                scope.launch {
                    _groupBookingResultFlow.emit(TokoChatResult.Success(channelUrl))
                }
            }
        }
    }

    fun cancel() {
        scope.cancel()
    }

    companion object {
        private const val CONVERSATION_NULL = "Conversation Repository is null"
    }
}
