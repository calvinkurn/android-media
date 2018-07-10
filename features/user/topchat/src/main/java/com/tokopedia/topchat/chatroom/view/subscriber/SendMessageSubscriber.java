package com.tokopedia.topchat.chatroom.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.listener.SendChat;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageSubscriber extends Subscriber<SendMessageViewModel> {

    private final SendChat.View view;

    public SendMessageSubscriber(SendChat.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.removeDummyMessage();
        view.setActionsEnabled(true);
        view.onErrorSendMessage(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SendMessageViewModel sendMessageViewModel) {
        view.removeDummyMessage();
        view.setActionsEnabled(true);
        if (sendMessageViewModel.isSuccess())
            view.onSuccessSendMessage();
        else
            view.onErrorSendMessage(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));


    }
}
