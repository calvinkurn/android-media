package com.tokopedia.tokochat.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetChatHistoryUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetTypingUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatMarkAsReadUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatRegistrationChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatSendMessageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokochat.domain.response.ticker.TokochatRoomTickerResponse
import com.tokopedia.tokochat.domain.usecase.GetTokoChatRoomTickerUseCase
import com.tokopedia.tokochat.domain.usecase.GetTokoChatBackgroundUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatMutationProfileUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoChatViewModel @Inject constructor(
    private val chatChannelUseCase: TokoChatChannelUseCase,
    private val getChatHistoryUseCase: TokoChatGetChatHistoryUseCase,
    private val markAsReadUseCase: TokoChatMarkAsReadUseCase,
    private val registrationChannelUseCase: TokoChatRegistrationChannelUseCase,
    private val sendMessageUseCase: TokoChatSendMessageUseCase,
    private val getTypingUseCase: TokoChatGetTypingUseCase,
    private val getTokoChatBackgroundUseCase: GetTokoChatBackgroundUseCase,
    private val getTokoChatRoomTickerUseCase: GetTokoChatRoomTickerUseCase,
    private val profileUseCase: TokoChatMutationProfileUseCase,
    private val dispatcher: CoroutineDispatchers
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

    private val _chatRoomTicker = MutableLiveData<Result<TokochatRoomTickerResponse>>()
    val chatRoomTicker: LiveData<Result<TokochatRoomTickerResponse>>
        get() = _chatRoomTicker

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun sendMessage(channelId: String, text: String) {
        try {
            val messageMetaData = SendMessageMetaData()
            sendMessageUseCase.sendTextMessage(
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
            chatChannelUseCase.initGroupBookingChat(
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
            chatChannelUseCase.getRemoteGroupBookingChannel(channelId, onSuccess = {
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
            registrationChannelUseCase.registerActiveChannel(channelId)
        } catch (throwable: Throwable) {
            _error.value = throwable
        }
    }

    fun deRegisterActiveChannel(channelId: String) {
        try {
            registrationChannelUseCase.deRegisterActiveChannel(channelId)
        } catch (throwable: Throwable) {
            _error.value = throwable
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
                    _isChatConnected.postValue(chatChannelUseCase.isChatConnected())
                    delay(5000)
                }
            }
        }, onError = {
            _isChatConnected.postValue(false)
        })
    }

    fun isChatConnected(): Boolean {
        return chatChannelUseCase.isChatConnected()
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

    fun loadChatRoomTicker() {
        launchCatchError(block = {
//            TODO: Change after BE side ready
//            val result = getTokoChatRoomTickerUseCase(GetTokoChatRoomTickerUseCase.PARAM_TOKOFOOD)
            val result = TokochatRoomTickerResponse().apply {
                this.tokochatRoomTicker.message = "Resto sudah terima pesananmu, jadi nggak bisa dibatalin. Driver hanya jemput & antar pesanan ke kamu."
                this.tokochatRoomTicker.tickerType = 0
            }
            _chatRoomTicker.value = Success(result)
        }, onError = {
            _chatRoomTicker.value = Fail(it)
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

    fun getMemberLeft(): MutableLiveData<String> {
        return try {
            chatChannelUseCase.getMemberLeftLiveData()
        } catch (throwable: Throwable) {
            _error.value = throwable
            MutableLiveData()
        }
    }
}
