package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatMemberUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {

    fun getMemberLeftLiveData(): MutableLiveData<String>? {
        return repository.getConversationRepository()?.getMemberLeftLiveDataCallback()
    }

    fun resetMemberLeftLiveData() {
        repository.getConversationRepository()?.resetMemberLeftLiveDataCallback()
    }
}
