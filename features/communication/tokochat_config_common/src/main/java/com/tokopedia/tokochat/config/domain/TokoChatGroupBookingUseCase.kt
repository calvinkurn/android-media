package com.tokopedia.tokochat.config.domain

import com.gojek.conversations.ConversationsRepository
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.config.util.TokoChatServiceType
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Predefined common usage for TokoChat related feature
 * Specific for initiation of channel (group booking)
 */
open class TokoChatGroupBookingUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {

    fun initGroupBookingChat(
        orderId: String,
        serviceType: TokoChatServiceType,
        orderChatType: OrderChatType = OrderChatType.Driver
    ): Flow<TokoChatResult<String>> {
        return flow {
            val conversationRepository = repository.getConversationRepository()
            if (conversationRepository != null) {
                getGroupBookingCallbackFlow(
                    conversationRepository,
                    orderId,
                    serviceType,
                    orderChatType
                )
            } else {
                emit(TokoChatResult.Error(MessageErrorException(CONVERSATION_NULL)))
            }
        }
    }

    fun getServiceType(source: String): TokoChatServiceType {
        return when (source) {
            SOURCE_GOSEND_INSTANT -> TokoChatServiceType.GOSEND_INSTANT
            SOURCE_GOSEND_SAMEDAY -> TokoChatServiceType.GOSEND_SAMEDAY
            else -> TokoChatServiceType.TOKOFOOD // default tokofood
        }
    }

    private fun getGroupBookingCallbackFlow(
        conversationRepository: ConversationsRepository,
        orderId: String,
        serviceType: TokoChatServiceType,
        orderChatType: OrderChatType
    ): Flow<TokoChatResult<String>> {
        return callbackFlow {
            val conversationCallback = getConversationGroupBookingListener(this)
            conversationRepository.initGroupBookingChat(
                orderId,
                serviceType.value,
                conversationCallback,
                orderChatType
            )
            awaitClose { channel.close() }
        }
    }

    private fun getConversationGroupBookingListener(
        scope: ProducerScope<TokoChatResult<String>>
    ): ConversationsGroupBookingListener {
        return object : ConversationsGroupBookingListener {
            override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
                scope.launch {
                    onGroupBookingError(scope, error)
                }
            }
            override fun onGroupBookingChannelCreationStarted() {
                scope.launch {
                    scope.send(TokoChatResult.Loading)
                }
            }
            override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
                scope.launch {
                    onGroupBookingSuccess(scope, channelUrl)
                }
            }
        }
    }

    private suspend fun onGroupBookingError(
        scope: ProducerScope<TokoChatResult<String>>,
        error: ConversationsNetworkError
    ) {
        Timber.d(error)
        scope.send(TokoChatResult.Error(error))
    }

    private suspend fun onGroupBookingSuccess(
        scope: ProducerScope<TokoChatResult<String>>,
        channelUrl: String
    ) {
        scope.send(TokoChatResult.Success(channelUrl))
    }

    companion object {
        private const val CONVERSATION_NULL = "Conversation Repository is null"

        const val SOURCE_GOSEND_INSTANT = "gosend_instant"
        const val SOURCE_GOSEND_SAMEDAY = "gosend_sameday"
    }
}
