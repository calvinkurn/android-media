package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.chatbot.domain.pojo.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.util.OptionListRecyclerItemDecorator
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist.ChatOptionListAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist.OPTION_TYPE_CSAT
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify

class CsatOptionListViewHolder(itemView: View,
                               private val csatOptionListListener: CsatOptionListListener,
                               chatLinkHandlerListener: ChatLinkHandlerListener,
                               chatbotAdapterListener: ChatbotAdapterListener
) : BaseChatBotViewHolder<CsatOptionsViewModel>(itemView, chatbotAdapterListener) {

    private val adapter: ChatOptionListAdapter
    private var model: CsatOptionsViewModel? = null
    private val chatActionListSelection: RecyclerView = itemView.findViewById<RecyclerView>(R.id.chat_csat_option_list_selection)
    private val chatActionBubbleSelectionContainer: CardUnify = itemView.findViewById<CardUnify>(R.id.chat_csat_option_list_container)
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    init {
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatOptionListAdapter(onOptionListSelected())
        chatActionListSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL, false)
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(OptionListRecyclerItemDecorator(itemView.context))

    }

    override fun bind(viewModel: CsatOptionsViewModel) {
        super.bind(viewModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(viewModel.message, customChatLayout, movementMethod)
        model = viewModel
        if (viewModel.isSubmited == true) {
            chatActionBubbleSelectionContainer.hide()
        } else {
            chatActionBubbleSelectionContainer.show()
            val options = getOptionListViewModelList(viewModel.csat?.points)
            adapter.setDataList(options)
        }
    }

    private fun getOptionListViewModelList(points: List<CsatAttributesPojo.Csat.Point>?): ArrayList<ChatOptionListViewModel> {
        val list = arrayListOf<ChatOptionListViewModel>()
        points?.forEach {
            val option = ChatOptionListViewModel()
            option.apply {
                text = it.caption ?: ""
                value = it.score
                type = OPTION_TYPE_CSAT
            }
            list.add(option)
        }
        return list

    }

    private fun onOptionListSelected(): (ChatOptionListViewModel) -> Unit = {
        csatOptionListListener.csatOptionListSelected(it, model)
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
        val LAYOUT = R.layout.chatbot_csat_option_layout
    }
}
