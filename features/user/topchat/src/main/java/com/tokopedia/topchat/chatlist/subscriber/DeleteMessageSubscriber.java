package com.tokopedia.topchat.chatlist.subscriber;

import android.util.Pair;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by stevenfredian on 10/30/17.
 */

public class DeleteMessageSubscriber extends Subscriber<DeleteChatListViewModel> {

    private InboxChatContract.View view;
    private List<Pair> originList;

    public DeleteMessageSubscriber(List<Pair> listMove, InboxChatContract.View view) {
        originList = listMove;
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorDeleteMessage(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(DeleteChatListViewModel listViewModel) {
        List<DeleteChatViewModel> list = listViewModel.getList();
        view.removeList(originList, list);
        view.reloadNotifDrawer();
    }

}

