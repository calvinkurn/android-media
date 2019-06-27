package com.tokopedia.topchat.chatlist.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.chatlist.viewmodel.EmptyChatModel;
import com.tokopedia.topchat.chatlist.fragment.InboxChatFragment;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.topchat.chatlist.adapter.viewholder.chatlist.EmptyChatListViewHolder;
import com.tokopedia.topchat.chatlist.adapter.viewholder.chatlist.ListChatViewHolder;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class InboxChatTypeFactoryImpl extends BaseAdapterTypeFactory implements InboxChatTypeFactory {

    InboxChatContract.View viewListener;
    InboxChatPresenter presenter;

    public InboxChatTypeFactoryImpl(InboxChatFragment context, InboxChatPresenter presenter) {
        this.viewListener = context;
        this.presenter = presenter;
    }


    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ListChatViewHolder.LAYOUT)
            viewHolder = new ListChatViewHolder(view, viewListener, presenter);
        else if (type == EmptyChatListViewHolder.LAYOUT)
            viewHolder = new EmptyChatListViewHolder(view, view.getContext());
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }

    @Override
    public int type(ChatListViewModel chatListViewModel) {
        return ListChatViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyChatModel emptyChatModel) {
        return EmptyChatListViewHolder.LAYOUT;
    }
}
