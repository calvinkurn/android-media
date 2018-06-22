package com.tokopedia.topchat.chatroom.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.presenter.ChatRoomPresenter;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;

import rx.Subscriber;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class GetReplySubscriber extends Subscriber<ChatRoomViewModel> {

    private final ChatRoomPresenter presenter;
    private ChatRoomContract.View view;


    public GetReplySubscriber(ChatRoomContract.View view, ChatRoomPresenter chatRoomPresenter) {
        this.view = view;
        presenter = chatRoomPresenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.setViewEnabled(true);
        view.showError(ErrorHandler.getErrorMessage(e));
        view.hideMainLoading();
        presenter.finishRequest();
    }

    @Override
    public void onNext(ChatRoomViewModel model) {
        view.setViewEnabled(true);
        if(model.getChatList().size()>0) view.setResult(model);
        else view.hideMainLoading();
        presenter.finishRequest();
    }
}
