package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class ChatViewHolder extends BaseChatViewHolder<ChatViewModel> {

    private TextView message;

    @LayoutRes
    public static final int LAYOUT = R.layout.chat_view_holder;

    public ChatViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    @Override
    public void bind(ChatViewModel element) {
        super.bind(element);
        message.setText(MethodChecker.fromHtml(element.getMessage()));

    }
}
