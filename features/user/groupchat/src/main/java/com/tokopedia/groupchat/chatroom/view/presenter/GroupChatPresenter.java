package com.tokopedia.groupchat.chatroom.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.groupchat.R;
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
    private String urlWebSocket;
    private LocalCacheHandler localCacheHandler;

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

    public void connectWebSocket(UserSession userSession, String channelUrl, String groupChatToken
            , SettingGroupChat settingGroupChat) {
        setUrlWebSocket(channelUrl);
        String magicString = "ws://172.31.4.23:8000/";

        magicString = getView().getContext().getSharedPreferences
                ("SP_REACT_DEVELOPMENT_MODE", Context.MODE_PRIVATE).getString("ip_groupchat", magicString);
        magicString = magicString.concat("/ws/groupchat?channel_id=96&token=").concat(groupChatToken);
        setUrlWebSocket(magicString);
        connect(userSession.getUserId(), userSession.getDeviceId(), userSession.getAccessToken(), urlWebSocket, settingGroupChat);
    }

    private void setUrlWebSocket(String channelUrl) {
        urlWebSocket = channelUrl;
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
                        if (getView() != null) {
                            String errorMessage = GroupChatErrorHandler.getErrorMessage(getView().getContext(), e, false);
                            String defaultMessage = getView().getContext().getString(R.string.default_request_error_unknown);
                            if(errorMessage.equals(defaultMessage)) {
                                getView().onErrorGetChannelInfo(getView().getContext().getString(R.string.default_error_enter_channel));
                            }else {
                                getView().onErrorGetChannelInfo(errorMessage);
                            }
                        }
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

    @Override
    public void detachView() {
        super.detachView();
        getChannelInfoUseCase.unsubscribe();
        destroyWebSocket();
    }


    private void connect(String userId, String deviceId, String accessToken, String channelUrl
            , SettingGroupChat settingGroupChat) {
        if (mSubscription == null || mSubscription.isUnsubscribed()) {
            mSubscription = new CompositeSubscription();
        }

        if(settingGroupChat == null){
            settingGroupChat = new SettingGroupChat();
        }

        getView().setSnackBarErrorLoading();

        SettingGroupChat finalSettingGroupChat = settingGroupChat;
        WebSocketSubscriber subscriber = new WebSocketSubscriber() {
            @Override
            protected void onOpen(@NonNull WebSocket webSocket) {
                showDummy("onOpened ".concat(channelUrl), "logger open");
                getView().onOpenWebSocket();
                Log.d("RxWebSocket Presenter", " on WebSocket open");
                setReportWebSocket(false);
            }

            @Override
            protected void onMessage(@NonNull String text) {
                Log.d("RxWebSocket Presenter", text);
                showDummy(text, "logger message");
            }

            @Override
            protected void onMessage(@NonNull Visitable item, boolean hideMessage) {
                Log.d("RxWebSocket Presenter", "item");
                getView().onMessageReceived(item, hideMessage);
            }

            @Override
            protected void onMessage(@NonNull ByteString byteString) {
                Log.d("RxWebSocket Presenter", byteString.toString());
            }

            @Override
            protected void onReconnect() {
                Log.d("RxWebSocket Presenter", "onReconnect");
                showDummy("reconnecting", "logger reconnect");
                getView().setSnackBarErrorLoading();
            }

            @Override
            protected void onClose() {
                Log.d("RxWebSocket Presenter", "onClose");
                showDummy("onClose", "logger close");
                destroyWebSocket();
                connect(userId, deviceId, accessToken, channelUrl, finalSettingGroupChat);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.d("RxWebSocket Presenter", "onError " + e.toString());
                showDummy(e.toString(), "logger error");
                getView().setSnackBarRetry();
                reportWebSocket(e);
            }
        };
        Subscription subscription = RxWebSocket.get(channelUrl, accessToken,
                    settingGroupChat.getDelay(), settingGroupChat.getMaxRetries()
                , settingGroupChat.getPingInterval()).subscribe(subscriber);


        if (subscriber != null) {
            mSubscription.add(subscription);
        }
    }

    public void testSendReply(PendingChatViewModel pendingChatViewModel) {
        Exception errorSendIndicator = null;
        try {
            RxWebSocket.send(urlWebSocket, GroupChatWebSocketParam.getParamSend("96", pendingChatViewModel.getMessage()));
        } catch (WebSocketException e) {
            errorSendIndicator = e;
            showDummy(e.toString(), "error logger send");
        }

        getView().afterSendMessage(pendingChatViewModel, errorSendIndicator);
        showDummy(pendingChatViewModel.getMessage(), "logger send");
    }

    public void destroyWebSocket() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private void showDummy(String message, String senderName) {
        boolean showLog = getView().getContext().getSharedPreferences
                ("SP_REACT_DEVELOPMENT_MODE", Context.MODE_PRIVATE).getBoolean("log_groupchat", false);
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
        if(shouldReportWebSocket()){
            getView().reportWebSocket(urlWebSocket, e.toString());
            setReportWebSocket(false);
        }
    }
}
