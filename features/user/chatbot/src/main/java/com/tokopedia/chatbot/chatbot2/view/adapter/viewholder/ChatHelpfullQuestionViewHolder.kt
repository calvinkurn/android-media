package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.data.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.helpfullquestionoptionlist.ChatOptionListAdapter
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.util.decorator.OptionListRecyclerItemDecorator
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify

class ChatHelpfullQuestionViewHolder(
    itemView: View,
    private val chatOptionListListener: ChatOptionListListener,
    chatLinkHandlerListener: ChatLinkHandlerListener,
    chatbotAdapterListener: ChatbotAdapterListener
) : BaseChatBotViewHolder<HelpFullQuestionsUiModel>(itemView, chatbotAdapterListener) {

    private val adapter: ChatOptionListAdapter
    private var model: HelpFullQuestionsUiModel? = null
    private var chatActionListSelection: RecyclerView = itemView.findViewById(
        R.id.chat_option_list_selection
    )
    private var chatActionBubbleSelectionContainer: CardUnify = itemView.findViewById(
        R.id.chat_option_list_container
    )
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    init {
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatOptionListAdapter(onOptionListSelected())
        chatActionListSelection.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(OptionListRecyclerItemDecorator(itemView.context))
    }

    override fun bind(viewModel: HelpFullQuestionsUiModel) {
        super.bind(viewModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(
            viewModel.message,
            customChatLayout,
            movementMethod
        )
        model = viewModel
        if (viewModel.isSubmited) {
            chatActionBubbleSelectionContainer.hide()
        } else {
            chatActionBubbleSelectionContainer.show()
            val options = getOptionListViewModelList(viewModel.helpfulQuestion?.helpfulQuestions)
            viewModel.helpfulQuestion?.helpfulQuestions?.let { adapter.setDataList(options) }
        }
    }

    private fun getOptionListViewModelList(
        helpfulQuestions: List<HelpFullQuestionPojo.HelpfulQuestion.HelpfulQuestions>?
    ): ArrayList<ChatOptionListUiModel> {
        val list = arrayListOf<ChatOptionListUiModel>()
        helpfulQuestions?.forEach {
            val option = ChatOptionListUiModel()
            option.apply {
                text = it.text ?: ""
                value = it.value ?: 0
            }
            list.add(option)
        }
        return list
    }

    private fun onOptionListSelected(): (ChatOptionListUiModel) -> Unit = {
        chatOptionListListener.chatOptionListSelected(it, model)
    }

    override fun onViewRecycled() {
        adapter.clearDataList()
        super.onViewRecycled()
    }

    override fun getCustomChatLayoutId(): Int = R.id.customChatLayout
    override fun getSenderAvatarId(): Int = R.id.senderAvatar
    override fun getSenderNameId(): Int = R.id.senderName
    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_helpful_question
    }
}
