package com.tokopedia.tokochat.config.domain

import androidx.lifecycle.asFlow
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Predefined common usage for TokoChat related feature
 * Specific for counter in general
 */
class TokoChatCounterUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {

    private val _unreadCounterFlow = MutableStateFlow<TokoChatResult<Int>>(TokoChatResult.Loading)
    val unreadCounterFlow = _unreadCounterFlow.asStateFlow()

    suspend fun fetchUnreadCount(channelId: String) {
        repository.getConversationRepository()
            ?.getUnreadCountForGroupBookings(channelId)
            ?.asFlow()
            ?.map { TokoChatResult.Success(it) }
            ?.catch { TokoChatResult.Error(it) }
            ?.collectLatest {
                _unreadCounterFlow.emit(it)
            }
    }
}
