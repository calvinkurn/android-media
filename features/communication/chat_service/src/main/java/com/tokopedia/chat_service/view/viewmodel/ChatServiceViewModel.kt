package com.tokopedia.chat_service.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.babble.channel.data.CreateChannelInfo
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_service.domain.CreateChannelUseCase
import com.tokopedia.chat_service.domain.GetChatHistoryUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatServiceViewModel @Inject constructor(
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val createChannelUseCase: CreateChannelUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _conversationsChannel = MutableLiveData<Result<ConversationsChannel>>()
    val conversationsChannel: LiveData<Result<ConversationsChannel>>
        get() = _conversationsChannel

    private val _conversationsMessage = MutableLiveData<Result<List<ConversationsMessage>>>()
    val conversationsMessage: LiveData<Result<List<ConversationsMessage>>>
        get() = _conversationsMessage

    fun createChannel(name: String, memberIds: List<String>, type: String, source: String) {
        val params = getChannelParam(name, memberIds, type, source)
        launchCatchError(block = {
            createChannelUseCase(params)
        }, onError = {
            params.onError(ConversationsNetworkError(it))
        })
    }

    private fun getChannelParam(
        name: String,
        memberIds: List<String>,
        type: String,
        source: String
    ): CreateChannelUseCase.Param {
        return CreateChannelUseCase.Param(
            createChannelInfo = CreateChannelInfo(
                name, memberIds, type, source
            ),
            onSuccess = {
                _conversationsChannel.value = Success(it)
            },
            onError = {
                _conversationsChannel.value = Fail(it)
            }
        )
    }

    fun getChatHistory(channelUrl: String) {
        launchCatchError(block = {
            val response = getChatHistoryUseCase(channelUrl)
            _conversationsMessage.value = Success(response)
        }, onError = {
            _conversationsMessage.value = Fail(it)
        })
    }
}