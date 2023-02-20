package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import androidx.lifecycle.LiveData
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

class GetUnreadChatCountUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    fun unReadCount(channelId: String): LiveData<Int> {
        return repository.getConversationRepository().getUnreadCountForGroupBookings(channelId)
    }
}
