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
import com.tokopedia.chatbot.ChatbotConstant.RENDER_TO_UI_BASED_ON_STATUS
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotBubbleActionSelectionListBinding
import com.tokopedia.chatbot.util.OptionListRecyclerItemDecorator
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist.ChatActionBubbleAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionListBubbleViewHolder(itemView: View, private val viewListener: ChatActionListBubbleListener, chatLinkHandlerListener: ChatLinkHandlerListener) :
    BaseChatBotViewHolder<ChatActionSelectionBubbleUiModel>(itemView), ChatActionBubbleAdapter.OnChatActionSelectedListener {
    private val adapter: ChatActionBubbleAdapter
    private var model: ChatActionSelectionBubbleUiModel? = null
    var viewBinding = ItemChatbotBubbleActionSelectionListBinding.bind(itemView)
    private var chatActionListSelection: RecyclerView = viewBinding.chatActionBubbleSelection
    private var chatActionListSelectionContainer: LinearLayout = viewBinding.chatActionBubbleSelectionContainer
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    private val bg = ViewUtil.generateBackgroundWithShadow(
        chatActionListSelectionContainer,
        R.color.chatbot_dms_left_message_bg,
        R.dimen.dp_chatbot_0,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER
    )

    init {
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatActionBubbleAdapter(this)
        chatActionListSelection.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(OptionListRecyclerItemDecorator(itemView.context))
    }

    override fun bind(viewModel: ChatActionSelectionBubbleUiModel) {
        if (viewModel.status == RENDER_TO_UI_BASED_ON_STATUS) {
            super.bind(viewModel)
            bindActionBubbleBackground()
            ChatbotMessageViewHolderBinder.bindChatMessage(
                viewModel.message,
                customChatLayout,
                movementMethod
            )
            model = viewModel
            adapter.setDataList(viewModel.chatActionList)
            chatActionListSelection.show()
        } else {
            chatActionListSelection.hide()
        }
    }

    private fun bindActionBubbleBackground() {
        chatActionListSelectionContainer.setActionBubbleBackground(bg)
    }

    override fun onChatActionSelected(selected: ChatActionBubbleUiModel) {
        model?.let {
            viewListener.onChatActionBalloonSelected(selected, it)
        }
    }

    override fun onViewRecycled() {
        adapter.clearDataList()
        super.onViewRecycled()
    }

    override fun getCustomChatLayoutId(): Int = R.id.customChatLayout

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_bubble_action_selection_list
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
