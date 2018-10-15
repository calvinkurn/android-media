package com.tokopedia.groupchat.chatroom.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sendbird.android.OpenChannel;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.groupchat.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.GetChannelInfoUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.LogoutGroupChatUseCase;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.websocket.GroupChatWebSocketParam;
import com.tokopedia.groupchat.chatroom.websocket.RxWebSocket;
import com.tokopedia.groupchat.chatroom.websocket.WebSocketSubscriber;
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
    private final LoginGroupChatUseCase loginGroupChatUseCase;
    private final LogoutGroupChatUseCase logoutGroupChatUseCase;
    private final ChannelHandlerUseCase channelHandlerUseCase;
    private CompositeSubscription mSubscription;

    @Inject
    public GroupChatPresenter(LoginGroupChatUseCase loginGroupChatUseCase,
                              GetChannelInfoUseCase getChannelInfoUseCase,
                              LogoutGroupChatUseCase logoutGroupChatUseCase,
                              ChannelHandlerUseCase channelHandlerUseCase) {
        this.getChannelInfoUseCase = getChannelInfoUseCase;
        this.loginGroupChatUseCase = loginGroupChatUseCase;
        this.logoutGroupChatUseCase = logoutGroupChatUseCase;
        this.channelHandlerUseCase = channelHandlerUseCase;
    }

    @Override
    public void enterChannel(String userId, String channelUrl, String userName, String userAvatar,
                             LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener, String sendBirdToken, String deviceId, String accessToken) {
        loginGroupChatUseCase.execute(getView().getContext(), channelUrl, userId, userName,
                userAvatar, loginGroupChatListener, sendBirdToken);
        connect(userId, deviceId, accessToken, channelUrl);
    }

    @Override
    public void refreshChannelInfo(String channelUuid) {
        getChannelInfoUseCase.execute(GetChannelInfoUseCase.createParams(channelUuid, true), new Subscriber<ChannelInfoViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().onErrorGetChannelInfo(GroupChatErrorHandler.getErrorMessage(
                            getView().getContext(), e, false
                    ));
                }
            }

            @Override
            public void onNext(ChannelInfoViewModel channelInfoViewModel) {
                if (getView() != null && channelInfoViewModel.isFreeze()) {
                    getView().onChannelFrozen();
                } else {
                    getView().onSuccessRefreshChannelInfo(channelInfoViewModel);
                }
            }
        });
    }

    @Override
    public void logoutChannel(OpenChannel mChannel) {
        logoutGroupChatUseCase.execute(mChannel);
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
                            getView().onErrorGetChannelInfo(GroupChatErrorHandler.getErrorMessage(
                                    getView().getContext(), e, false
                            ));
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

    public void setHandler(String channelUrl, String channelHandlerId, ChannelHandlerUseCase.ChannelHandlerListener listener) {
        channelHandlerUseCase.execute(channelUrl, channelHandlerId, listener);
    }

    public void enterChannelAfterRefresh(String userId, String channelUrl, String userName, String
            userAvatar, LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener, String sendBirdToken, String deviceId, String accessToken) {
        loginGroupChatUseCase.execute(getView().getContext(), channelUrl, userId, userName,
                userAvatar, new LoginGroupChatUseCase.LoginGroupChatListener() {
                    @Override
                    public void onSuccessEnterChannel(OpenChannel openChannel) {
                        getView().onSuccessEnterRefreshChannel(openChannel);

                    }

                    @Override
                    public void onErrorEnterChannel(String errorMessage) {
                        loginGroupChatListener.onErrorEnterChannel(errorMessage);
                    }

                    @Override
                    public void onUserBanned(String errorMessage) {

                    }

                    @Override
                    public void onChannelNotFound(String sendBirdErrorMessage) {

                    }
                }, sendBirdToken);
        connect(userId, deviceId, accessToken, channelUrl);
    }

    private void connect(String userId, String deviceId, String accessToken, String channelUrl) {
        if (mSubscription == null || mSubscription.isUnsubscribed()) {
            mSubscription = new CompositeSubscription();
        }

        String magicString = "wss://chat.tokopedia.com" +
                "/connect" +
                "?os_type=1" +
                "&device_id=" + deviceId +
                "&user_id=" + userId;

        magicString = "ws://172.28.0.12/ws/groupchat?channel_id=96";
        magicString = "ws://172.31.4.23:8000/ws/groupchat?channel_id=96";

        magicString = getView().getContext().getSharedPreferences
                ("SP_REACT_DEVELOPMENT_MODE", Context.MODE_PRIVATE).getString("ip_groupchat", magicString);
        magicString = magicString.concat("/ws/groupchat?channel_id=96");

        boolean showLog = getView().getContext().getSharedPreferences
                ("SP_REACT_DEVELOPMENT_MODE", Context.MODE_PRIVATE).getBoolean("log_groupchat", false);
        String finalMagicString = magicString;
        WebSocketSubscriber subscriber = new WebSocketSubscriber() {
            @Override
            protected void onOpen(@NonNull WebSocket webSocket) {
                if(showLog){
                    ChatViewModel dummy = new ChatViewModel(
                            "onOpened ".concat(finalMagicString),
                            System.currentTimeMillis(),
                            System.currentTimeMillis(),
                            String.valueOf(1231),
                            "123321",
                            "opeeen",
                            "https://imagerouter.tokopedia.com/img/100-square/user-1/2018/3/16/7822796/7822796_9963cc8f-91c7-4b0e-9ec5-ea538953995c.jpg",
                            false,
                            false
                    );
                    getView().onMessageReceived(dummy);
                }

                Log.d("RxWebSocket Presenter", " on WebSocket open");
            }

            @Override
            protected void onMessage(@NonNull String text) {
                if(showLog){
                    ChatViewModel dummy = new ChatViewModel(
                            text,
                            System.currentTimeMillis(),
                            System.currentTimeMillis(),
                            String.valueOf(1231),
                            "123321",
                            "logger",
                            "https://imagerouter.tokopedia.com/img/100-square/user-1/2018/3/16/7822796/7822796_9963cc8f-91c7-4b0e-9ec5-ea538953995c.jpg",
                            false,
                            false
                    );
                    getView().onMessageReceived(dummy);
                }
                Log.d("RxWebSocket Presenter", text);
            }

            @Override
            protected void onMessage(@NonNull Visitable item) {
                Log.d("RxWebSocket Presenter", "item");
                getView().onMessageReceived(item);
            }

            @Override
            protected void onMessage(@NonNull ByteString byteString) {
                Log.d("RxWebSocket Presenter", byteString.toString());
            }

            @Override
            protected void onReconnect() {
                Log.d("RxWebSocket Presenter", "onReconnect");
            }

            @Override
            protected void onClose() {
                Log.d("RxWebSocket Presenter", "onClose");
                destroyWebSocket();
                connect(userId, deviceId, accessToken, channelUrl);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.d("RxWebSocket Presenter", "onError " + e.toString());
            }
        };
        Subscription subscription = RxWebSocket.get(magicString, accessToken)
                .subscribe(subscriber);


        if (subscriber != null) {
            mSubscription.add(subscription);
        }
    }

    public void testSendReply(PendingChatViewModel pendingChatViewModel, String channelUrl, UserSession userSession) {
        String START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat date = new SimpleDateFormat(START_TIME_FORMAT, Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startTime = date.format(Calendar.getInstance().getTime());

        String magicString = "wss://chat.tokopedia.com" +
                "/connect" +
                "?os_type=1" +
                "&device_id=" + userSession.getDeviceId() +
                "&user_id=" + userSession.getUserId();
        magicString = "ws://172.28.0.12/ws/groupchat?channel_id=96";
        magicString = "ws://172.31.4.23:8000";
        magicString = getView().getContext().getSharedPreferences
                ("SP_REACT_DEVELOPMENT_MODE", Context.MODE_PRIVATE).getString("ip_groupchat", magicString);
        magicString = magicString.concat("/ws/groupchat?channel_id=96");
        boolean showLog = getView().getContext().getSharedPreferences
                ("SP_REACT_DEVELOPMENT_MODE", Context.MODE_PRIVATE).getBoolean("log_groupchat", false);


        try {
            RxWebSocket.send(magicString, GroupChatWebSocketParam.getParamSend("96", pendingChatViewModel.getMessage()));
        }catch (Exception e){
            if(showLog){
                ChatViewModel dummy = new ChatViewModel(
                        e.toString(),
                        System.currentTimeMillis(),
                        System.currentTimeMillis(),
                        String.valueOf(1231),
                        "123321",
                        "error logger",
                        "https://imagerouter.tokopedia.com/img/100-square/user-1/2018/3/16/7822796/7822796_9963cc8f-91c7-4b0e-9ec5-ea538953995c.jpg",
                        false,
                        false
                );
                getView().onMessageReceived(dummy);
            }
        }

        if(showLog){
            ChatViewModel dummy = new ChatViewModel(
                    pendingChatViewModel.getMessage(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    String.valueOf(1231),
                    "123321",
                    "logger",
                    "https://imagerouter.tokopedia.com/img/100-square/user-1/2018/3/16/7822796/7822796_9963cc8f-91c7-4b0e-9ec5-ea538953995c.jpg",
                    false,
                    false
            );
            getView().onMessageReceived(dummy);
        }
    }

    public void destroyWebSocket() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
