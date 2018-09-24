package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.chatactionbubblelist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot
        .AttachedInvoiceSelectionViewHolder;
import com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble.ChatActionBubbleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendri on 18/07/18.
 */
public class ChatActionBubbleAdapter extends RecyclerView.Adapter<ChatActionBubbleViewHolder>  {
    private ArrayList<ChatActionBubbleViewModel> data = new ArrayList<>();
    private OnChatActionSelectedListener listener;

    public ChatActionBubbleAdapter(OnChatActionSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatActionBubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_action_bubble, parent, false);
        ChatActionBubbleViewHolder vh = new ChatActionBubbleViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatActionBubbleViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.itemView.setOnClickListener((View v) -> listener.onChatActionSelected(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearDataList(){
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0,size);
    }

    public void setDataList(List<ChatActionBubbleViewModel> elements){
        data.clear();
        data.addAll(elements);
        notifyDataSetChanged();
    }

    public interface OnChatActionSelectedListener{
        void onChatActionSelected(ChatActionBubbleViewModel selected);
    }
}
