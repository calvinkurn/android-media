package com.tokopedia.tokochat.domain

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import javax.inject.Inject

class GetTypingUseCase@Inject constructor(
    private val repository: TokoChatRepository
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
