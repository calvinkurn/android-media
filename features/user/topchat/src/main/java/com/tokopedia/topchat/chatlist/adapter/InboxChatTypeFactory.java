package com.tokopedia.topchat.chatlist.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.chatlist.viewmodel.EmptyChatModel;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface InboxChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(ChatListViewModel chatListViewModel);

    int type(EmptyChatModel emptyChatModel);
}
