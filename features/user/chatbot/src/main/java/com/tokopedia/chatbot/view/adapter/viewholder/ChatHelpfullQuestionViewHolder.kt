package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist.ChatOptionListAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify

class ChatHelpfullQuestionViewHolder(itemView: View, private val chatOptionListListener: ChatOptionListListener) : BaseChatViewHolder<HelpFullQuestionsViewModel>(itemView) {

    private val adapter: ChatOptionListAdapter
    private var model: HelpFullQuestionsViewModel? = null
    private var chatActionListSelection: RecyclerView = itemView.findViewById<RecyclerView>(R.id.chat_option_list_selection)
    private var chatActionBubbleSelectionContainer: CardUnify = itemView.findViewById<CardUnify>(R.id.chat_option_list_container)
    private var mesage: TextView = itemView.findViewById<TextView>(R.id.message)


    init {
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatOptionListAdapter(onOptionListSelected())
        chatActionListSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL, false)
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(DividerItemDecoration(itemView.context))

    }

    override fun bind(viewModel: HelpFullQuestionsViewModel?) {
        super.bind(viewModel)
        model = viewModel
        mesage.text = viewModel?.message
        if (viewModel?.isSubmited == true){
            chatActionBubbleSelectionContainer.hide()
        }else{
            chatActionBubbleSelectionContainer.show()
            viewModel?.helpfulQuestion?.helpfulQuestions?.let { adapter.setDataList(it) }
        }
    }

    private fun onOptionListSelected(): (HelpFullQuestionPojo.HelpfulQuestion.HelpfulQuestions) -> Unit = {
        chatOptionListListener.chatOptionListSelected(it, model)
    }

    override fun onViewRecycled() {
        adapter.clearDataList()
        super.onViewRecycled()
    }


    override fun getHourId(): Int {
        return R.id.hour
    }

    override fun getDateId(): Int {
        return R.id.date
    }

    override fun getHourTime(replyTime: String): String {
        return ChatBotTimeConverter.getHourTime(replyTime)
    }

    override fun alwaysShowTime(): Boolean = true

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.chatbot_helpfull_question_layout
    }


}