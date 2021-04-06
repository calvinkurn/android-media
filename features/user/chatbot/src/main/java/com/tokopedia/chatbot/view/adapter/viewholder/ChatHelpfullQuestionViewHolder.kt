package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.util.OptionListRecyclerItemDecorator
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist.ChatOptionListAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify

class ChatHelpfullQuestionViewHolder(itemView: View,
                                     private val chatOptionListListener: ChatOptionListListener,
                                     chatLinkHandlerListener: ChatLinkHandlerListener,
                                     chatbotAdapterListener: ChatbotAdapterListener) : BaseChatBotViewHolder<HelpFullQuestionsViewModel>(itemView, chatbotAdapterListener) {

    private val adapter: ChatOptionListAdapter
    private var model: HelpFullQuestionsViewModel? = null
    private var chatActionListSelection: RecyclerView = itemView.findViewById<RecyclerView>(R.id.chat_option_list_selection)
    private var chatActionBubbleSelectionContainer: CardUnify = itemView.findViewById<CardUnify>(R.id.chat_option_list_container)
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    init {
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatOptionListAdapter(onOptionListSelected())
        chatActionListSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL, false)
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(OptionListRecyclerItemDecorator(itemView.context))

    }

    override fun bind(viewModel: HelpFullQuestionsViewModel) {
        super.bind(viewModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(viewModel.message, customChatLayout, movementMethod)
        model = viewModel
        if (viewModel.isSubmited == true) {
            chatActionBubbleSelectionContainer.hide()
        } else {
            chatActionBubbleSelectionContainer.show()
            val options = getOptionListViewModelList(viewModel.helpfulQuestion?.helpfulQuestions)
            viewModel.helpfulQuestion?.helpfulQuestions?.let { adapter.setDataList(options) }
        }
    }

    private fun getOptionListViewModelList(helpfulQuestions: List<HelpFullQuestionPojo.HelpfulQuestion.HelpfulQuestions>?): ArrayList<ChatOptionListViewModel> {
        val list = arrayListOf<ChatOptionListViewModel>()
        helpfulQuestions?.forEach {
            val option = ChatOptionListViewModel()
            option.apply {
                text = it.text ?: ""
                value = it.value ?: 0
            }
            list.add(option)
        }
        return list
    }

    private fun onOptionListSelected(): (ChatOptionListViewModel) -> Unit = {
        chatOptionListListener.chatOptionListSelected(it, model)
    }

    override fun onViewRecycled() {
        adapter.clearDataList()
        super.onViewRecycled()
    }

    override fun getCustomChatLayoutId(): Int =  com.tokopedia.chatbot.R.id.customChatLayout
    override fun getSenderAvatarId(): Int = R.id.senderAvatar
    override fun getSenderNameId(): Int = R.id.senderName
    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.chatbot_helpfull_question_layout
    }

}