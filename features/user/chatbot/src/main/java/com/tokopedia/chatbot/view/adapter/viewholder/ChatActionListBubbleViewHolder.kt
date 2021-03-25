package com.tokopedia.chatbot.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.util.OptionListRecyclerItemDecorator
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist.ChatActionBubbleAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionListBubbleViewHolder(itemView: View, private val viewListener: ChatActionListBubbleListener, chatLinkHandlerListener: ChatLinkHandlerListener)
    : BaseChatBotViewHolder<ChatActionSelectionBubbleViewModel>(itemView), ChatActionBubbleAdapter.OnChatActionSelectedListener {
    private val adapter: ChatActionBubbleAdapter
    private var model: ChatActionSelectionBubbleViewModel? = null
    private var chatActionListSelection: RecyclerView = itemView.findViewById<RecyclerView>(R.id.chat_action_bubble_selection)
    private var chatActionListSelectionContainer: LinearLayout = itemView.findViewById<LinearLayout>(R.id.chat_action_bubble_selection_container)
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            chatActionListSelectionContainer,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
    )

    init {
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatActionBubbleAdapter(this)
        chatActionListSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL, false)
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(OptionListRecyclerItemDecorator(itemView.context))

    }

    override fun bind(viewModel: ChatActionSelectionBubbleViewModel) {
        super.bind(viewModel)
        bindActionBubbleBackground()
        ChatbotMessageViewHolderBinder.bindChatMessage(viewModel.message, customChatLayout, movementMethod)
        model = viewModel
        adapter.setDataList(viewModel.chatActionList)
    }

    private fun bindActionBubbleBackground() {
        chatActionListSelectionContainer.setActionBubbleBackground(bg)
    }

    override fun onChatActionSelected(selected: ChatActionBubbleViewModel) {
        model?.let {
            viewListener.onChatActionBalloonSelected(selected, it)
        }
    }

    override fun onViewRecycled() {
        adapter.clearDataList()
        super.onViewRecycled()
    }

    override fun getCustomChatLayoutId(): Int = com.tokopedia.chatbot.R.id.customChatLayout

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chat_action_bubble_selection_list
    }
}

private fun View.setActionBubbleBackground(bg: Drawable?) {
    val pl = paddingLeft
    val pt = paddingTop
    val pr = paddingRight
    val pb = paddingBottom
    setBackground(bg)
    setPadding(pl, pt, pr, pb)
}
