package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;


/**
 * Created by stevenfredian on 11/29/17.
 */

public class QuickReplyItemViewHolder extends AbstractViewHolder<GroupChatQuickReplyItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_quick_reply;
    private final ChatroomContract.QuickReply viewListener;
    private TextView textHolder;


    public QuickReplyItemViewHolder(View parent, ChatroomContract.QuickReply listener) {
        super(parent);
        textHolder = itemView.findViewById(R.id.text);
        this.viewListener = listener;
    }

    @Override
    public void bind(final GroupChatQuickReplyItemViewModel element) {

        textHolder.setText(Html.fromHtml(element.getText()));

        textHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.addQuickReply(element.getText());
            }
        });

        textHolder.setVisibility(View.VISIBLE);
    }
}
