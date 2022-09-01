package com.tokopedia.chat_service.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.gojek.conversations.babble.channel.data.CreateChannelInfo
import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.extensions.ExtensionMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_service.domain.CreateChannelUseCase
import com.tokopedia.chat_service.domain.GetAllChannelsUseCase
import com.tokopedia.chat_service.domain.GetChatHistoryUseCase
import com.tokopedia.chat_service.domain.MarkAsReadUseCase
import com.tokopedia.chat_service.domain.RegistrationActiveChannelUseCase
import com.tokopedia.chat_service.domain.SendMessageUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatServiceViewModel @Inject constructor(
    private val createChannelUseCase: CreateChannelUseCase,
    private val getAllChannelsUseCase: GetAllChannelsUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val registrationActiveChannelUseCase: RegistrationActiveChannelUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _conversationsChannel = MutableLiveData<Result<ConversationsChannel>>()
    val conversationsChannel: LiveData<Result<ConversationsChannel>>
        get() = _conversationsChannel

    private val _channelUrl = MutableLiveData<String>()
    val conversationsMessage: LiveData<List<ConversationsMessage>> =
        Transformations.switchMap(_channelUrl) {
            getChatHistoryUseCase(it)
        }

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun createChannel(name: String, memberIds: List<String>, type: String, source: String) {
        val params = getChannelParam(name, memberIds, type, source)
        try {
            createChannelUseCase(params)
        } catch (throwable: Throwable) {
            params.onError(ConversationsNetworkError(throwable))
        }
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

    fun setChannelUrl(channelUrl: String) {
        _channelUrl.value = channelUrl
    }

    fun registerActiveChannel() {
        try {
            val channelUrl = _channelUrl.value
            registrationActiveChannelUseCase.registerActiveChannel(channelUrl!!)
        } catch (throwable: Throwable) {
            Log.d("RegisterActiveChannel", "Error - ${throwable.message}")
            _error.value = throwable
        }
    }

    fun deRegisterActiveChannel() {
        try {
            val channelUrl = _channelUrl.value
            registrationActiveChannelUseCase.deRegisterActiveChannel(channelUrl!!)
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

    fun sendMessage(text: String) {
        try {
            val channelUrl = _channelUrl.value
            val messageMetaData = SendMessageMetaData()
            sendMessageUseCase.sendTextMessage(
                channelUrl!!,
                text,
                messageMetaData
            )
        } catch (throwable: Throwable) {
            Log.d("SendMessage", "Error - ${throwable.message}")
            _error.value = throwable
        }
    }

    fun sendExtensionMessage() {
        try {
            val channelUrl = _channelUrl.value
            val extensionMessage = getExtensionMessage()
            sendMessageUseCase.sendExtensionMessage(
                channelUrl!!,
                extensionMessage,
                onSuccess = {
                    Log.d("SendExtensionMsg", "Success")
                },
                onError = {
                    Log.d("SendExtensionMsg", "$it")
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

    fun markChatAsRead() {
        try {
            val channelUrl = _channelUrl.value
            markAsReadUseCase(channelUrl!!)
        } catch (throwable: Throwable) {
            Log.d("MarkAsRead", "Error - ${throwable.message}")
            _error.value = throwable
        }
    }

    fun initGroupBooking(
        orderId: String,
        serviceType: Int,
        orderChatType: OrderChatType
    ) {
        try {
            createChannelUseCase.initGroupBookingChat(
                orderId = orderId,
                serviceType = serviceType,
                groupBookingListener = getGroupBookingListener(),
                orderChatType = orderChatType
            )
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    private fun getGroupBookingListener(): ConversationsGroupBookingListener {
        return object : ConversationsGroupBookingListener {
            override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
                Log.d("GroupBooking", "Error - ${error.errorList}")
                error.printStackTrace()
                _error.value = error
            }

            override fun onGroupBookingChannelCreationStarted() {
                Log.d("GroupBooking", "Started")
            }

            override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
                Log.d("GroupBooking", "Success - channelUrl : $channelUrl")
            }

        }
    }
}