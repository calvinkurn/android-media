package com.tokopedia.tokochat.stub.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.domain.usecase.TokoChatRoomUseCase
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import javax.inject.Inject

class TokoChatRoomUseCaseStub @Inject constructor(
    @ActivityScope tokoChatRepositoryStub: TokoChatRepositoryStub
) : TokoChatRoomUseCase(tokoChatRepositoryStub) {

    var isConnected: Boolean = true
    var conversationsChannel: ConversationsChannel? = null

    override fun isChatConnected(): Boolean {
        return isConnected
    }

    override fun getLiveChannel(channelId: String): LiveData<ConversationsChannel?>? {
        return if (conversationsChannel == null) {
            super.getLiveChannel(channelId)
        } else {
            MutableLiveData(conversationsChannel)
        }
    }
}
