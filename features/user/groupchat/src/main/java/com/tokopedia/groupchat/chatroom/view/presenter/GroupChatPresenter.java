package com.tokopedia.groupchat.chatroom.view.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sendbird.android.OpenChannel;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.groupchat.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.GetChannelInfoUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.LogoutGroupChatUseCase;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.websocket.RxWebSocket;
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
        connect(userId, deviceId, accessToken);
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
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
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
        connect(userId, deviceId, accessToken);
    }

    private void connect(String userId, String deviceId, String accessToken) {
        if (mSubscription == null || mSubscription.isUnsubscribed()) {
            mSubscription = new CompositeSubscription();
        }

        String magicString = "wss://chat.tokopedia.com" +
                "/connect" +
                "?os_type=1" +
                "&device_id=" + deviceId +
                "&user_id=" + userId;

        WebSocketSubscriber subscriber = new WebSocketSubscriber() {
            @Override
            protected void onOpen(@NonNull WebSocket webSocket) {
                Log.d("MainActivity", " on WebSocket open");
            }

            @Override
            protected void onMessage(@NonNull String text) {
                Log.d("MainActivity", text);
            }

            @Override
            protected void onMessage(@NonNull ByteString byteString) {
                Log.d("MainActivity", byteString.toString());
            }

            @Override
            protected void onReconnect() {
                Log.d("MainActivity", "onReconnect");
            }

            @Override
            protected void onClose() {
                Log.d("MainActivity", "onClose");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        };
        Subscription subscription = RxWebSocket.get(magicString, accessToken)
                //RxLifecycle : https://github.com/dhhAndroid/RxLifecycle
                .subscribe(subscriber);


        if (subscriber != null) {
            mSubscription.add(subscription);
        }
    }
}
