package com.tokopedia.tokochat.config.domain

import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.config.util.asFlowResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Predefined common usage for TokoChat related feature
 * Specific for counter in general
 */
class TokoChatCounterUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {

    fun fetchUnreadCount(channelId: String): Flow<TokoChatResult<Int>> {
        return flow {
            val conversationRepository = repository.getConversationRepository()
            if (conversationRepository != null) {
                emitAll(
                    conversationRepository
                        .getUnreadCountForGroupBookings(channelId)
                        .asFlowResult()
                )
            }
        }
    }
}
