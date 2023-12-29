package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatGetTypingUseCase@Inject constructor(
    private val repository: TokoChatRepository
) {
    fun getTypingStatus(): MutableLiveData<List<String>>? =
        repository.getConversationRepository()?.getTypingStatusCallback()

    fun resetTypingStatus() {
        repository.getConversationRepository()?.resetTypingStatusCallback()
    }

    fun setTypingStatus(status: Boolean) {
        repository.getConversationRepository()?.setTypingStatus(status)
    }
}
