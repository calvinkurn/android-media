package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import androidx.lifecycle.LiveData
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

open class GetUnreadChatCountUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    fun unReadCount(channelId: String): LiveData<Int>? {
        return repository.getConversationRepository()?.getUnreadCountForGroupBookings(channelId)
    }
}
