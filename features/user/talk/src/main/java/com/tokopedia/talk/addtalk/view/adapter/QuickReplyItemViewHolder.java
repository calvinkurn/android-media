package com.tokopedia.talk.addtalk.view.adapter;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.talk.addtalk.view.listener.AddTalkContract;


/**
 * Created by stevenfredian on 11/29/17.
 */

public class QuickReplyItemViewHolder extends AbstractViewHolder<TalkQuickReplyItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_quick_reply;
    private final ChatroomContract.View viewListener;
    private TextView textHolder;


    public QuickReplyItemViewHolder(View parent, AddTalkContract.View listener) {
        super(parent);
        textHolder = itemView.findViewById(R.id.text);
        this.viewListener = listener;
    }

    @Override
    public void bind(final TalkQuickReplyItemViewModel element) {

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
