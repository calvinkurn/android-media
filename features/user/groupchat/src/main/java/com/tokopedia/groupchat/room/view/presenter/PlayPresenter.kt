package com.tokopedia.groupchat.room.view.presenter

import android.util.Log
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat
import com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayCloseViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.chatroom.websocket.GroupChatWebSocketParam
import com.tokopedia.groupchat.room.domain.mapper.PlayWebSocketMessageMapper
import com.tokopedia.groupchat.room.domain.usecase.GetPlayInfoUseCase
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketException
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.WebSocket
import okio.ByteString
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author : Steven 13/02/19
 */

class PlayPresenter @Inject constructor(
        var getPlayInfoUseCase: GetPlayInfoUseCase,
        var webSocketMessageMapper: PlayWebSocketMessageMapper)
    : BaseDaggerPresenter<PlayContract.View>(), PlayContract.Presenter{

    private var mSubscription: CompositeSubscription? = null
    private var localCacheHandler: LocalCacheHandler? = null

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        localCacheHandler = LocalCacheHandler(view.context, GroupChatPresenter::class.java.name)
    }

    fun getPlayInfo(channelId: String?, onSuccessGetInfo: (ChannelInfoViewModel) -> Unit) {
        getPlayInfoUseCase.execute(
                GetPlayInfoUseCase.createParams(channelId),
                object : Subscriber<ChannelInfoViewModel>() {
                    override fun onNext(t: ChannelInfoViewModel?) {
                        if(t != null) {
                            onSuccessGetInfo(t)
                        }else {
                            view.onChannelDeleted()
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("tevplay", e.toString())
                    }

                })
    }


    override fun openWebSocket(userSession: UserSessionInterface, channelId: String, groupChatToken: String, settingGroupChat: SettingGroupChat?) {
        var settings = settingGroupChat ?: SettingGroupChat()
        processUrl(userSession, channelId, groupChatToken, settings)
        connectWebSocket(userSession.userId, userSession.deviceId, userSession.accessToken, settings, groupChatToken)
    }

    private fun connectWebSocket(userId: String?, deviceId: String?, accessToken: String, settings: SettingGroupChat, groupChatToken: String) {

        if (mSubscription == null || mSubscription!!.isUnsubscribed) {
            mSubscription = CompositeSubscription()
        }

        val subscriber = object : WebSocketSubscriber() {
            override fun onOpen(webSocket: WebSocket) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", " on WebSocket open")
//                    showDummy("onOpened $webSocketUrlWithToken", "logger open")
                }
                view.onOpenWebSocket()
            }

            override fun onMessage(text: String) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", text)
//                    showDummy(text, "logger message")
                }
            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "item")
                }

                var item = webSocketMessageMapper.map(webSocketResponse)
                var hideMessage = webSocketMessageMapper.mapHideMessage(webSocketResponse)
                item?.let {
                    when (it) {
                        is ParticipantViewModel -> view.onTotalViewChanged(it)
                        is VibrateViewModel -> view.vibratePhone()
                        is AdsViewModel -> view.onAdsUpdated(it)
                        is PinnedMessageViewModel -> view.onPinnedMessageUpdated(it)
                        is VideoViewModel -> view.onVideoUpdated(it)
                        is EventGroupChatViewModel -> view.handleEvent(it)
                        is GroupChatQuickReplyViewModel -> view.onQuickReplyUpdated(it)
                        is OverlayViewModel -> view.showOverlayDialog(it)
                        is OverlayCloseViewModel -> view.closeOverlayDialog()
                        is ButtonsPojo -> view.updateDynamicButton(it)
                        else -> {view.addIncomingMessage(it)}
                    }
                }
            }

            override fun onMessage(byteString: ByteString) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", byteString.toString())
                }
            }

            override fun onReconnect() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onReconnect")
                    showDummy("reconnecting", "logger reconnect")
                }
                view.setSnackBarConnectingWebSocket()
            }

            override fun onClose() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onClose")
                    showDummy("onClose", "logger close")
                }
                destroyWebSocket()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onError " + e.toString())
                    showDummy(e.toString(), "logger error")
                }
                view.setSnackBarRetryConnectingWebSocket()
//                reportWebSocket(e)
            }
        }
        val rxWebSocket = RxWebSocket[
                webSocketUrlWithToken,
                accessToken
//                , settings.delay,
//                settings.maxRetries,
//                settings.pingInterval,
//                groupChatToken
        ]
        val subscription = rxWebSocket?.subscribe(subscriber)

        mSubscription!!.add(subscription)
    }

    private lateinit var channelId: String
    private lateinit var webSocketUrl: String
    private lateinit var webSocketUrlWithToken: String

    private fun processUrl(userSession: UserSessionInterface, channelId: String, groupChatToken: String?, settingGroupChat: SettingGroupChat?) {
        var magicString = ChatroomUrl.GROUP_CHAT_WEBSOCKET_DOMAIN
        magicString = localCacheHandler?.getString("ip_groupchat", magicString)

        this.webSocketUrl = String.format("%s%s%s", magicString, ChatroomUrl.PATH_WEB_SOCKET_GROUP_CHAT_URL, channelId)
        this.webSocketUrlWithToken = String.format("%s%s%s", webSocketUrl, "&token=", groupChatToken)
        this.channelId = channelId

    }

    private fun showDummy(message: String?, senderName: String) {
//        val showLog = localCacheHandler.getBoolean("log_groupchat", false)!!
//        if (showLog!!) {
//            val dummy = ChatViewModel(
//                    message!!,
//                    System.currentTimeMillis(),
//                    System.currentTimeMillis(),
//                    1231.toString(),
//                    "123321",
//                    senderName,
//                    "https://vignette.wikia.nocookie.net/supersentaibattlediceo/images/7/7a/Engine-O_G12.jpg/revision/latest?cb=20120718165720&format=original",
//                    false,
//                    false
//            )
//            view.onMessageReceived(dummy, !showLog!!)
//        }
    }

    fun destroyWebSocket() {
        mSubscription?.clear()
    }

    override fun detachView() {
        super.detachView()
        getPlayInfoUseCase.unsubscribe()
        destroyWebSocket()
    }

    override fun sendMessage(
            viewModel: PendingChatViewModel,
            afterSendMessage: () -> Unit,
            onSuccessSendMessage: (PendingChatViewModel) -> Unit,
            onErrorSendMessage: (PendingChatViewModel, Exception?) -> Unit) {
        var errorSendIndicator: Exception? = null
        try {
            RxWebSocket.send(GroupChatWebSocketParam.getParamSend(channelId, viewModel.message), null)
        } catch (e: WebSocketException) {
            errorSendIndicator = e
            showDummy(e.toString(), "error logger send")
        }

        if(errorSendIndicator == null){
            onSuccessSendMessage(viewModel)
        } else if(errorSendIndicator !is WebSocketException){
            onErrorSendMessage(viewModel, errorSendIndicator)
        } else if(errorSendIndicator is WebSocketException) {
            view.setSnackBarRetryConnectingWebSocket()
        }


        afterSendMessage()
    }
}