package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.ChatViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.QuickReplyItemViewHolder;
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyViewModel;

/**
 * @author by StevenFredian on 07/06/18.
 */

public class QuickReplyTypeFactoryImpl extends BaseAdapterTypeFactory implements QuickReplyTypeFactory {


    private final ChatroomContract.View listener;

    public QuickReplyTypeFactoryImpl(GroupChatFragment fragment) {
        listener = fragment;
    }

    @Override
    public int type(GroupChatQuickReplyViewModel groupChatQuickReplyViewModel) {
        return QuickReplyItemViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;

        if (type == QuickReplyItemViewHolder.LAYOUT) {
            viewHolder = new QuickReplyItemViewHolder(parent, listener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }

        return viewHolder;
    }
}
