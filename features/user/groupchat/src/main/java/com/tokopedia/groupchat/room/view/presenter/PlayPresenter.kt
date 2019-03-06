package com.tokopedia.groupchat.room.view.presenter

import android.util.Log
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat
import com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayCloseViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.chatroom.websocket.GroupChatWebSocketParam
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler
import com.tokopedia.groupchat.room.domain.mapper.PlayWebSocketMessageMapper
import com.tokopedia.groupchat.room.domain.usecase.GetDynamicButtonsUseCase
import com.tokopedia.groupchat.room.domain.usecase.GetPlayInfoUseCase
import com.tokopedia.groupchat.room.domain.usecase.GetStickyComponentUseCase
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
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
        var getDynamicButtonsUseCase: GetDynamicButtonsUseCase,
        var getStickyComponentUseCase: GetStickyComponentUseCase,
        var webSocketMessageMapper: PlayWebSocketMessageMapper)
    : BaseDaggerPresenter<PlayContract.View>(), PlayContract.Presenter {

    private var mSubscription: CompositeSubscription? = null
    private var localCacheHandler: LocalCacheHandler? = null

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        localCacheHandler = LocalCacheHandler(view.context, GroupChatPresenter::class.java.name)
    }

    override fun getPlayInfo(channelId: String?, onSuccessGetInfo: (ChannelInfoViewModel) -> Unit,
                             onErrorGetInfo: (String) -> Unit) {
        getPlayInfoUseCase.execute(
                GetPlayInfoUseCase.createParams(channelId),
                object : Subscriber<ChannelInfoViewModel>() {
                    override fun onNext(t: ChannelInfoViewModel?) {
                        if (t != null) {
                            onSuccessGetInfo(t)
                        } else {
                            view.onChannelDeleted()
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        val errorMessage = GroupChatErrorHandler.getErrorMessage(view.context, e, false)
                        val defaultMessage = view.context.getString(R.string.default_request_error_unknown)
                        val internalServerErrorMessage = "Internal Server Error"
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            onErrorGetInfo(e.toString())
                        } else if (errorMessage == defaultMessage || errorMessage.equals
                                (internalServerErrorMessage, ignoreCase = true)) {
                            onErrorGetInfo(view.context.getString(R.string.default_error_enter_channel))
                        } else {
                            onErrorGetInfo(errorMessage)
                        }
                    }

                })
    }

    override fun getDynamicButtons(channelId: String?,
                                   onSuccessGetDynamicButtons: (DynamicButtonsViewModel) -> Unit,
                                   onErrorGetDynamicButtons: (String) -> Unit) {
        getDynamicButtonsUseCase.execute(
                GetDynamicButtonsUseCase.createParams(channelId),
                object : Subscriber<DynamicButtonsViewModel>() {
                    override fun onNext(t: DynamicButtonsViewModel?) {
                        if (t != null) {
                            onSuccessGetDynamicButtons(t)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        val errorMessage = GroupChatErrorHandler.getErrorMessage(view.context, e, false)
                        onErrorGetDynamicButtons(errorMessage)
                    }
                }
        )
    }

    override fun getStickyComponents(channelId: String?,
                                     onSuccessGetStickyComponent: (StickyComponentViewModel) -> Unit,
                                     onErrorGetStickyComponent: (String) -> Unit) {

        getStickyComponentUseCase.execute(
                GetDynamicButtonsUseCase.createParams(channelId),
                object : Subscriber<StickyComponentViewModel>() {
                    override fun onNext(t: StickyComponentViewModel?) {
                        if (t != null) {
                            onSuccessGetStickyComponent(t)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        val errorMessage = GroupChatErrorHandler.getErrorMessage(view.context, e, false)
                        onErrorGetStickyComponent(errorMessage)
                    }
                }
        )
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
                        is DynamicButtonsViewModel -> view.updateDynamicButton(it)
                        is BackgroundViewModel -> view.onBackgroundUpdated(it)
                        is SprintSaleAnnouncementViewModel -> view.onSprintSaleReceived(it)
                        is StickyComponentViewModel -> view.onStickyComponentReceived(it)
                        else -> {
                            view.addIncomingMessage(it)
                        }
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
            }
        }
        val rxWebSocket = RxWebSocket[
                webSocketUrlWithToken,
                accessToken,
                settings.delay,
                settings.pingInterval,
                settings.maxRetries
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
//To be used for debug
    }

    fun destroyWebSocket() {
        mSubscription?.clear()
    }

    override fun detachView() {
        super.detachView()
        getPlayInfoUseCase.unsubscribe()
        getDynamicButtonsUseCase.unsubscribe()
        getStickyComponentUseCase.unsubscribe()
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

        if (errorSendIndicator == null) {
            onSuccessSendMessage(viewModel)
        } else if (errorSendIndicator !is WebSocketException) {
            onErrorSendMessage(viewModel, errorSendIndicator)
        } else if (errorSendIndicator is WebSocketException) {
            view.setSnackBarRetryConnectingWebSocket()
        }


        afterSendMessage()
    }
}