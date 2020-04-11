package com.tokopedia.chatbot.view.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist.ChatActionBubbleAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionListBubbleViewHolder(itemView: View, private val viewListener: ChatActionListBubbleListener)
    : BaseChatViewHolder<ChatActionSelectionBubbleViewModel>(itemView), ChatActionBubbleAdapter.OnChatActionSelectedListener {
    private val adapter: ChatActionBubbleAdapter
    private var model: ChatActionSelectionBubbleViewModel? = null
    private var chatActionListSelection:RecyclerView
    private var mTime:TextView


    init {
        chatActionListSelection = itemView.findViewById<RecyclerView>(R.id.chat_action_bubble_selection)
        mTime = itemView.findViewById(R.id.hour)
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatActionBubbleAdapter(this)
        chatActionListSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL, false)
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(DividerItemDecoration(itemView.context))

        hour = itemView.findViewById(hourId)
    }

    override fun getHourId(): Int {
        return R.id.hour
    }

    override fun bind(viewModel: ChatActionSelectionBubbleViewModel) {
        super.bind(viewModel)
        chatActionListSelection.show()
        hour.show()
        model = viewModel
        adapter.setDataList(viewModel.chatActionList)
    }

    override fun onChatActionSelected(selected: ChatActionBubbleViewModel) {
        model?.let {
            viewListener.onChatActionBalloonSelected(selected, it)
        }
        chatActionListSelection.hide()
        hour.hide()
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
