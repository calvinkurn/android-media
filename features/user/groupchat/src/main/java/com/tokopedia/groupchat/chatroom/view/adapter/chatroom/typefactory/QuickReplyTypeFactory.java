package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyViewModel;

/**
 * @author by StevenFredian on 07/06/18.
 */

public interface QuickReplyTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(GroupChatQuickReplyViewModel groupChatQuickReplyViewModel);
}
