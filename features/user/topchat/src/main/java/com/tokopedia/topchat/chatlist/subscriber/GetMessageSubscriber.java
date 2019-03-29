package com.tokopedia.topchat.chatlist.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import rx.Subscriber;

/**
 * Created by stevenfredian on 10/19/17.
 */

public class GetMessageSubscriber extends Subscriber<InboxChatViewModel> {

    private InboxChatPresenter presenter;
    private InboxChatContract.View view;

    public GetMessageSubscriber(InboxChatContract.View view, InboxChatPresenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        presenter.setRequesting(false);
        presenter.setError(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(InboxChatViewModel messageData) {
        presenter.setRequesting(false);
        view.enableActions();
        view.finishLoading();

        view.setResultFetch(messageData);
        presenter.prepareNextPage(messageData.isHasNext());
        view.saveResult();

        view.setMustRefresh(false);
    }
}
