package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.chatactionbubblelist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble.ChatActionBubbleViewModel;

/**
 * Created by Hendri on 18/07/18.
 */
public class ChatActionBubbleViewHolder extends RecyclerView.ViewHolder{
    private TextView chatActionMessage;
    public ChatActionBubbleViewHolder(View itemView) {
        super(itemView);
        chatActionMessage = itemView.findViewById(R.id.chat_action_message);
    }

    public void bind(ChatActionBubbleViewModel element){
        chatActionMessage.setText(element.getMessage());
    }
}
