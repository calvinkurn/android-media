package com.tokopedia.tokochat.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.babble.channel.data.CreateChannelInfo
import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.extensions.ExtensionMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.domain.usecase.CreateChannelUseCase
import com.tokopedia.tokochat.domain.usecase.GetAllChannelsUseCase
import com.tokopedia.tokochat.domain.usecase.GetChatHistoryUseCase
import com.tokopedia.tokochat.domain.usecase.GetTypingUseCase
import com.tokopedia.tokochat.domain.usecase.MarkAsReadUseCase
import com.tokopedia.tokochat.domain.usecase.RegistrationActiveChannelUseCase
import com.tokopedia.tokochat.domain.usecase.MutationTokoChatMessageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokochat.domain.usecase.GetTokoChatBackgroundUseCase
import com.tokopedia.tokochat.domain.usecase.MutationProfileUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoChatViewModel @Inject constructor(
    private val createChannelUseCase: CreateChannelUseCase,
    private val getAllChannelsUseCase: GetAllChannelsUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val registrationActiveChannelUseCase: RegistrationActiveChannelUseCase,
    private val mutationTokoChatMessageUseCase: MutationTokoChatMessageUseCase,
    private val getTypingUseCase: GetTypingUseCase,
    private val getTokoChatBackgroundUseCase: GetTokoChatBackgroundUseCase,
    private val profileUseCase: MutationProfileUseCase,
    private val dispatcher: CoroutineDispatchers,
): BaseViewModel(dispatcher.main) {

    private val _channelDetail = MutableLiveData<Result<GroupBookingChannelDetails>>()
    val channelDetail: LiveData<Result<GroupBookingChannelDetails>>
        get() = _channelDetail

    private val _isChatConnected = MutableLiveData<Boolean>()
    val isChatConnected: LiveData<Boolean>
        get() = _isChatConnected

    private val _chatBackground = MutableLiveData<Result<String>>()
    val chatBackground: MutableLiveData<Result<String>>
        get() = _chatBackground

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun sendMessage(channelId: String, text: String) {
        try {
            val messageMetaData = SendMessageMetaData()
            mutationTokoChatMessageUseCase.sendTextMessage(
                channelId,
                text,
                messageMetaData
            )
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun initGroupBooking(
        orderId: String,
        serviceType: Int = 2,
        groupBookingListener: ConversationsGroupBookingListener,
        orderChatType: OrderChatType = OrderChatType.Unknown
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

    fun getGroupBookingChannel(channelId: String) {
        try {
            createChannelUseCase.getGroupBookingChannel(channelId, onSuccess = {
                _channelDetail.postValue(Success(it))
            }, onError = {
                _channelDetail.postValue(Fail(it))
            })
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun getChatHistory(channelId: String): LiveData<List<ConversationsMessage>> {
        return try {
            getChatHistoryUseCase(channelId)
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

    fun markChatAsRead(channelId: String) {
        try {
            markAsReadUseCase(channelId)
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun registerActiveChannel(channelId: String) {
        try {
            registrationActiveChannelUseCase.registerActiveChannel(channelId)
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun deRegisterActiveChannel(channelId: String) {
        try {
            registrationActiveChannelUseCase.deRegisterActiveChannel(channelId)
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
                while (true) {
                    _isChatConnected.postValue(createChannelUseCase.isChatConnected())
                    delay(5000)
                }
            }
        }, onError = {
            _isChatConnected.postValue(false)
        })
    }

    fun isChatConnected(): Boolean {
        return createChannelUseCase.isChatConnected()
    }

    fun getTotalUnreadCount(): LiveData<Int> {
        return try {
            getChatHistoryUseCase.getTotalUnreadCount(listOf(ChannelType.GroupBooking))
        } catch (throwable: Throwable) {
            _error.value = throwable
            MutableLiveData()
        }
    }

    fun getTokoChatBackground() {
        launchCatchError(block = {
            getTokoChatBackgroundUseCase(Unit).collect {
                _chatBackground.value = Success(it)
            }
        }, onError = {
            _chatBackground.value = Fail(it)
        })
    }

    fun initializeProfile() {
        try {
            profileUseCase.initializeConversationProfile()
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun getUserId(): String {
        return try {
            profileUseCase.getUserId()
        } catch (throwable: Throwable) {
            _error.value = throwable
            ""
        }
    }
}
