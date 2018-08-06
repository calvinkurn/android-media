package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot;

import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.chatactionbubblelist
        .ChatActionBubbleAdapter;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble.ChatActionBubbleViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble.ChatActionSelectionBubbleViewModel;

/**
 * Created by Hendri on 18/07/18.
 */
public class ChatActionListBubbleViewHolder extends BaseChatViewHolder<ChatActionSelectionBubbleViewModel>
                                         implements ChatActionBubbleAdapter.OnChatActionSelectedListener{
    private RecyclerView chatActionListSelection;
    private ChatActionBubbleAdapter adapter;
    private ChatActionSelectionBubbleViewModel model;
    @LayoutRes
    public static final int LAYOUT = R.layout.item_chat_action_bubble_selection_list;

    public ChatActionListBubbleViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        chatActionListSelection = itemView.findViewById(R.id.chat_action_bubble_selection);
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection,false);
        adapter = new ChatActionBubbleAdapter(this);
        chatActionListSelection.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.VERTICAL, false));
        chatActionListSelection.setAdapter(adapter);
        chatActionListSelection.addItemDecoration(new DividerItemDecoration(itemView.getContext()));
    }

    @Override
    public void bind(ChatActionSelectionBubbleViewModel viewModel) {
        super.bind(viewModel);
        model = viewModel;
        adapter.setDataList(viewModel.getChatActionList());
    }

    @Override
    public void onChatActionSelected(ChatActionBubbleViewModel selected) {
        viewListener.onChatActionBalloonSelected(selected,model);
    }

    @Override
    public void onViewRecycled() {
        adapter.clearDataList();
        super.onViewRecycled();
    }
}
