package com.tokopedia.chatbot.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist.ChatActionBubbleAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionListBubbleViewHolder(itemView: View, private val viewListener: ChatActionListBubbleListener)
    : BaseChatViewHolder<ChatActionSelectionBubbleViewModel>(itemView), ChatActionBubbleAdapter.OnChatActionSelectedListener {
    private val adapter: ChatActionBubbleAdapter
    private var model: ChatActionSelectionBubbleViewModel? = null

    init {
        val chatActionListSelection = itemView.findViewById<RecyclerView>(R.id.chat_action_bubble_selection)
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatActionBubbleAdapter(this)
        chatActionListSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL, false)
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(DividerItemDecoration(itemView.context))
    }

    override fun bind(viewModel: ChatActionSelectionBubbleViewModel) {
        super.bind(viewModel)
        model = viewModel
        adapter.setDataList(viewModel.chatActionList)
    }

    override fun onChatActionSelected(selected: ChatActionBubbleViewModel) {
        viewListener.onChatActionBalloonSelected(selected, model!!)
    }

    override fun onViewRecycled() {
        adapter.clearDataList()
        super.onViewRecycled()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chat_action_bubble_selection_list
    }
}
