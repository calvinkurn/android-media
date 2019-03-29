package com.tokopedia.topchat.chatlist.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import rx.Subscriber;

/**
 * Created by stevenfredian on 10/19/17.
 */

public class SearchMessageSubscriber extends Subscriber<InboxChatViewModel> {

    private InboxChatPresenter presenter;
    private InboxChatContract.View view;

    public SearchMessageSubscriber(InboxChatContract.View view, InboxChatPresenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        presenter.setRequesting(false);
        view.finishSearch();
        view.showError(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(InboxChatViewModel inboxChatViewModel) {
        presenter.setRequesting(false);
        view.finishSearch();
        view.setResultSearch(inboxChatViewModel);
        presenter.prepareNextPage(inboxChatViewModel.isHasNextReplies());
    }

}
