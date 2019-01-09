package com.tokopedia.groupchat.chatroom.view.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat;
import com.tokopedia.groupchat.chatroom.domain.usecase.GetChannelInfoUseCase;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.websocket.GroupChatWebSocketParam;
import com.tokopedia.groupchat.chatroom.websocket.RxWebSocket;
import com.tokopedia.groupchat.chatroom.websocket.WebSocketException;
import com.tokopedia.groupchat.chatroom.websocket.WebSocketSubscriber;
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler;
import com.tokopedia.network.constant.TkpdBaseURL;

import javax.inject.Inject;

import okhttp3.WebSocket;
import okio.ByteString;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by nisie on 3/21/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetChannelInfoUseCase getChannelInfoUseCase;
    private CompositeSubscription mSubscription;
    private String webSocketUrl;
    private String webSocketUrlWithToken;
    private LocalCacheHandler localCacheHandler;
    private String channelId;

    @Inject
    public GroupChatPresenter(
            GetChannelInfoUseCase getChannelInfoUseCase) {
        this.getChannelInfoUseCase = getChannelInfoUseCase;
    }

    @Override
    public void attachView(GroupChatContract.View view) {
        super.attachView(view);
        localCacheHandler = new LocalCacheHandler(getView().getContext(), GroupChatPresenter.class.getName());
    }

    public void connectWebSocket(UserSession userSession, String channelId, String groupChatToken
            , SettingGroupChat settingGroupChat) {
        String magicString = ChatroomUrl.GROUP_CHAT_WEBSOCKET_DOMAIN;
        magicString = localCacheHandler.getString("ip_groupchat", magicString);

        this.webSocketUrlWithToken = (String.format("%s%s%s%s%s", magicString, ChatroomUrl
                .PATH_WEB_SOCKET_GROUP_CHAT_URL, channelId, "&token=", groupChatToken));
        this.webSocketUrl = (String.format("%s%s%s", magicString, ChatroomUrl.PATH_WEB_SOCKET_GROUP_CHAT_URL, channelId));
        this.channelId = channelId;
        connect(userSession.getUserId(), userSession.getDeviceId(), userSession.getAccessToken(),
                settingGroupChat, groupChatToken);
    }

    @Override
    public void getChannelInfo(String channelUuid) {
        getChannelInfo(channelUuid, false);
    }

    @Override
    public void getChannelInfo(String channelUuid, boolean reInit) {
        getChannelInfoUseCase.execute(GetChannelInfoUseCase.createParams(channelUuid),
                new Subscriber<ChannelInfoViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onErrorGetChannelInfo(e);
                    }

                    @Override
                    public void onNext(ChannelInfoViewModel channelInfoViewModel) {
                        if (getView() != null && channelInfoViewModel.isFreeze()) {
                            getView().onChannelFrozen();
                        } else if (getView() != null) {
                            getView().onSuccessGetChannelInfo(channelInfoViewModel);
                        }
                    }
                });
    }

    private void onErrorGetChannelInfo(Throwable e) {
        if (getView() != null) {
            String errorMessage = GroupChatErrorHandler.getErrorMessage(getView().getContext(), e, false);
            String defaultMessage = getView().getContext().getString(R.string.default_request_error_unknown);
            String internalServerErrorMessage = "Internal Server Error";
            if (errorMessage.equals(defaultMessage) || errorMessage.equalsIgnoreCase(internalServerErrorMessage)) {
                getView().onErrorGetChannelInfo(getView().getContext().getString(R.string.default_error_enter_channel));
            } else {
                getView().onErrorGetChannelInfo(errorMessage);
            }
        }
    }

    public void refreshChannelInfo(String channelUuid) {
        getChannelInfoUseCase.execute(GetChannelInfoUseCase.createParams(channelUuid),
                new Subscriber<ChannelInfoViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onErrorGetChannelInfo(e);
                    }

                    @Override
                    public void onNext(ChannelInfoViewModel channelInfoViewModel) {
                        if (getView() != null && channelInfoViewModel.isFreeze()) {
                            getView().onChannelFrozen();
                        } else if (getView() != null) {
                            getView().onSuccessRefreshChannelInfo(channelInfoViewModel);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        getChannelInfoUseCase.unsubscribe();
        destroyWebSocket();
    }


    private void connect(String userId, String deviceId, String accessToken
            , SettingGroupChat settingGroupChat, String groupChatToken) {

        destroyWebSocket();

        if (mSubscription == null || mSubscription.isUnsubscribed()) {
            mSubscription = new CompositeSubscription();
        }

        if (settingGroupChat == null) {
            settingGroupChat = new SettingGroupChat();
        }

        getView().setSnackBarErrorLoading();

        SettingGroupChat finalSettingGroupChat = settingGroupChat;
        WebSocketSubscriber subscriber = new WebSocketSubscriber() {
            @Override
            protected void onOpen(@NonNull WebSocket webSocket) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", " on WebSocket open");
                    showDummy("onOpened ".concat(webSocketUrlWithToken), "logger open");
                }
                getView().onOpenWebSocket();
                setReportWebSocket(false);
            }

            @Override
            protected void onMessage(@NonNull String text) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", text);
                    showDummy(text, "logger message");
                }
            }

            @Override
            protected void onMessage(@NonNull Visitable item, boolean hideMessage) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "item");
                }
                getView().onMessageReceived(item, hideMessage);
            }

            @Override
            protected void onMessage(@NonNull ByteString byteString) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", byteString.toString());
                }
            }

            @Override
            protected void onReconnect() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onReconnect");
                    showDummy("reconnecting", "logger reconnect");
                }
                getView().setSnackBarErrorLoading();
            }

            @Override
            protected void onClose() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onClose");
                    showDummy("onClose", "logger close");
                }
                destroyWebSocket();
                connect(userId, deviceId, accessToken, finalSettingGroupChat, groupChatToken);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onError " + e.toString());
                    showDummy(e.toString(), "logger error");
                }
                getView().setSnackBarRetry();
                reportWebSocket(e);
            }
        };
        Subscription subscription = RxWebSocket.get(webSocketUrl, accessToken,
                settingGroupChat.getDelay(), settingGroupChat.getMaxRetries()
                , settingGroupChat.getPingInterval(), groupChatToken).subscribe(subscriber);

        mSubscription.add(subscription);
    }

    public void sendViaWebSocket(PendingChatViewModel pendingChatViewModel) {
        Exception errorSendIndicator = null;
        try {
            RxWebSocket.send(webSocketUrl, GroupChatWebSocketParam.getParamSend(channelId, pendingChatViewModel.getMessage()));
        } catch (WebSocketException e) {
            errorSendIndicator = e;
            showDummy(e.toString(), "error logger send");
        }

        getView().afterSendMessage(pendingChatViewModel, errorSendIndicator);
        showDummy(pendingChatViewModel.getMessage(), "logger send");
    }

    public void destroyWebSocket() {
        if (mSubscription != null) {
            mSubscription.clear();
        }
    }

    private void showDummy(String message, String senderName) {
        boolean showLog = localCacheHandler.getBoolean("log_groupchat", false);
        if (showLog) {
            ChatViewModel dummy = new ChatViewModel(
                    message,
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    String.valueOf(1231),
                    "123321",
                    senderName,
                    "https://vignette.wikia.nocookie.net/supersentaibattlediceo/images/7/7a/Engine-O_G12.jpg/revision/latest?cb=20120718165720&format=original",
                    false,
                    false
            );
            getView().onMessageReceived(dummy, !showLog);
        }
    }

    public boolean shouldReportWebSocket() {
        return localCacheHandler.getBoolean(GroupChatPresenter.class.getName(), false);
    }

    public void setReportWebSocket(boolean reportWebSocket) {
        localCacheHandler.putBoolean(GroupChatPresenter.class.getName(), reportWebSocket);
        localCacheHandler.applyEditor();
    }

    private void reportWebSocket(Throwable e) {
        if (shouldReportWebSocket()) {
            getView().reportWebSocket(webSocketUrl, e.toString());
            setReportWebSocket(false);
        }
    }


}
