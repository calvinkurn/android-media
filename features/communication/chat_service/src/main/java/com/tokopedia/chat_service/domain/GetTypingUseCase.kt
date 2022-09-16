package com.tokopedia.chat_service.domain

import androidx.lifecycle.MutableLiveData
import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import javax.inject.Inject

class GetTypingUseCase@Inject constructor(
    private val repository: ChatServiceRepository
) {
    fun getTypingStatus(): MutableLiveData<List<String>> =
        repository.getConversationRepository().getTypingStatusCallback()

    fun resetTypingStatus() {
        repository.getConversationRepository().resetTypingStatusCallback()
    }

    fun setTypingStatus(status: Boolean) {
        repository.getConversationRepository().setTypingStatus(status)
    }
}
