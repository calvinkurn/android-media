package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyViewModel;


/**
 * Created by stevenfredian on 11/29/17.
 */

public class QuickReplyItemViewHolder extends AbstractViewHolder<GroupChatQuickReplyItemViewModel> {

    private static int MAX_LENGTH = 8;
    private static int MIN_LENGTH = 5;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_quick_reply;
    private final ChatroomContract.View viewListener;
    private TextView textHolder;


    public QuickReplyItemViewHolder(View parent, ChatroomContract.View listener) {
        super(parent);
        textHolder = itemView.findViewById(R.id.text);
        this.viewListener = listener;
    }

    @Override
    public void bind(final GroupChatQuickReplyItemViewModel element) {

        String trimmed;

        if (element.getText().length() > MAX_LENGTH) {
            trimmed = element.getText().substring(0, MIN_LENGTH);
            trimmed = String.format("%s%s", trimmed, "...");
        } else {
            trimmed = element.getText();
        }

        textHolder.setText(Html.fromHtml(trimmed));

        textHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.addQuickReply(element.getText());
            }
        });

        textHolder.setVisibility(View.VISIBLE);
    }
}
