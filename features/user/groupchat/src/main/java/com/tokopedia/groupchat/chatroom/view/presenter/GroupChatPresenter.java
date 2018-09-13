package com.tokopedia.groupchat.chatroom.view.presenter;

import com.sendbird.android.OpenChannel;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.groupchat.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.GetChannelInfoUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.LogoutGroupChatUseCase;
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 3/21/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetChannelInfoUseCase getChannelInfoUseCase;
    private final LoginGroupChatUseCase loginGroupChatUseCase;
    private final LogoutGroupChatUseCase logoutGroupChatUseCase;
    private final ChannelHandlerUseCase channelHandlerUseCase;

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
                             LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener, String sendBirdToken) {
        loginGroupChatUseCase.execute(getView().getContext(), channelUrl, userId, userName,
                userAvatar, loginGroupChatListener, sendBirdToken);
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
    }

    public void setHandler(String channelUrl, String channelHandlerId, ChannelHandlerUseCase.ChannelHandlerListener listener) {
        channelHandlerUseCase.execute(channelUrl, channelHandlerId, listener);
    }

    public void enterChannelAfterRefresh(String userId, String channelUrl, String userName, String
            userAvatar, LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener, String sendBirdToken) {
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
    }
}
