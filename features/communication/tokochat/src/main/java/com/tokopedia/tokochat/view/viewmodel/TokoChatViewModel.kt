package com.tokopedia.tokochat.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.babble.channel.data.CreateChannelInfo
import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.extensions.ExtensionMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.domain.CreateChannelUseCase
import com.tokopedia.tokochat.domain.GetAllChannelsUseCase
import com.tokopedia.tokochat.domain.GetChatHistoryUseCase
import com.tokopedia.tokochat.domain.GetTypingUseCase
import com.tokopedia.tokochat.domain.MarkAsReadUseCase
import com.tokopedia.tokochat.domain.RegistrationActiveChannelUseCase
import com.tokopedia.tokochat.domain.SendMessageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoChatViewModel @Inject constructor(
    private val createChannelUseCase: CreateChannelUseCase,
    private val getAllChannelsUseCase: GetAllChannelsUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val registrationActiveChannelUseCase: RegistrationActiveChannelUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getTypingUseCase: GetTypingUseCase,
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _conversationsChannel = MutableLiveData<Result<ConversationsChannel>>()
    val conversationsChannel: LiveData<Result<ConversationsChannel>>
        get() = _conversationsChannel

    private val _isChatConnected = MutableLiveData<Boolean>()
    val isChatConnected: LiveData<Boolean>
        get() = _isChatConnected

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun sendMessage(channelUrl: String, text: String) {
        try {
            val messageMetaData = SendMessageMetaData()
            sendMessageUseCase.sendTextMessage(
                channelUrl,
                text,
                messageMetaData
            )
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun initGroupBooking(
        orderId: String,
        serviceType: Int,
        groupBookingListener: ConversationsGroupBookingListener,
        orderChatType: OrderChatType
    ) {
        try {
            createChannelUseCase.initGroupBookingChat(
                orderId = orderId,
                serviceType = serviceType,
                groupBookingListener = groupBookingListener,
                orderChatType = orderChatType
            )
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun getChatHistory(channelUrl: String): LiveData<List<ConversationsMessage>> {
        return try {
            getChatHistoryUseCase(channelUrl)
        } catch (throwable: Throwable) {
            _error.value = throwable
            MutableLiveData()
        }
    }

    fun loadPreviousMessages() {
        try {
            getChatHistoryUseCase.loadPreviousMessage()
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun markChatAsRead(channelUrl: String) {
        try {
            markAsReadUseCase(channelUrl)
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun registerActiveChannel(channelUrl: String) {
        try {
            registrationActiveChannelUseCase.registerActiveChannel(channelUrl)
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun deRegisterActiveChannel(channelUrl: String) {
        try {
            registrationActiveChannelUseCase.deRegisterActiveChannel(channelUrl)
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun getAllChannels(
        getChannelRequest: GetChannelRequest,
        onSuccess: (List<ConversationsChannel>) -> Unit,
        onError: (ConversationsNetworkError?) -> Unit
    ) {
        try {
            getAllChannelsUseCase(getChannelRequest,
                onSuccess = onSuccess,
                onError = onError
            )
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun getAllChannels(): LiveData<List<ConversationsChannel>> {
        return try {
            getAllChannelsUseCase()
        } catch (throwable: Throwable) {
            _error.value = throwable
            MutableLiveData()
        }
    }

    fun getTypingStatus(): LiveData<List<String>> {
        return try {
            getTypingUseCase.getTypingStatus()
        } catch (throwable: Throwable) {
            _error.value = throwable
            MutableLiveData()
        }
    }

    fun setTypingStatus(status: Boolean) {
        try {
            getTypingUseCase.setTypingStatus(status)
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun resetTypingStatus() {
        try {
            getTypingUseCase.resetTypingStatus()
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun doCheckChatConnection() {
        launchCatchError(context = dispatcher.io, block = {
            withContext(NonCancellable) {
                _isChatConnected.postValue(createChannelUseCase.isChatConnected())
                delay(5000)
            }
        }, onError = {
            _isChatConnected.postValue(false)
        })
    }

    fun getTotalUnreadCount(): LiveData<Int> {
        return try {
            getChatHistoryUseCase.getTotalUnreadCount()
        } catch (throwable: Throwable) {
            _error.value = throwable
            MutableLiveData()
        }
    }

    /**
     * Not P0
     */

    fun sendExtensionMessage(channelUrl: String) {
        try {
            val extensionMessage = getExtensionMessage()
            sendMessageUseCase.sendExtensionMessage(
                channelUrl,
                extensionMessage,
                onSuccess = {},
                onError = {
                    _error.value = it
                }
            )
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    private fun getExtensionMessage(): ExtensionMessage {
        return ExtensionMessage(
            extensionId = "",
            extensionMessageId = "",
            extensionVersion = 0,
            payload = "",
            message = "",
            messageId = "",
            senderId = "",
            transientId = null,
            extensionWidgetId = null,
            isCanned = null,
            cannedMessagePayload = null
        )
    }

    private fun getChannelParam(
        name: String,
        memberIds: List<String>,
        type: String,
        source: String
    ): CreateChannelUseCase.CreateChannelParam {
        return CreateChannelUseCase.CreateChannelParam(
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
}
