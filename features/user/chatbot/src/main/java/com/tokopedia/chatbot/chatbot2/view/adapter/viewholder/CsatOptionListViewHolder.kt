package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.data.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.helpfullquestionoptionlist.ChatOptionListAdapter
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.helpfullquestionoptionlist.OPTION_TYPE_CSAT
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.chatbot2.view.util.decorator.OptionListRecyclerItemDecorator
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackground
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify

class CsatOptionListViewHolder(
    itemView: View,
    private val csatOptionListListener: CsatOptionListListener,
    chatLinkHandlerListener: ChatLinkHandlerListener,
    chatbotAdapterListener: ChatbotAdapterListener
) : BaseChatBotViewHolder<CsatOptionsUiModel>(itemView, chatbotAdapterListener) {

    private val adapter: ChatOptionListAdapter
    private var model: CsatOptionsUiModel? = null
    private val chatActionListSelection: RecyclerView = itemView.findViewById(
        R.id.chat_csat_option_list_selection
    )
    private val chatActionBubbleSelectionContainer: CardUnify =
        itemView.findViewById(R.id.chat_csat_option_list_container)
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    private val bg = generateLeftMessageBackground(
        customChatLayout,
        R.color.chatbot_dms_left_message_bg,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
    )

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

    override fun bind(viewModel: CsatOptionsUiModel) {
        super.bind(viewModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(
            viewModel.message,
            customChatLayout,
            movementMethod
        )
        model = viewModel
        bindBackground()
        if (viewModel.isSubmited == true) {
            chatActionBubbleSelectionContainer.hide()
        } else {
            chatActionBubbleSelectionContainer.show()
            val options = getOptionListViewModelList(viewModel.csat?.points)
            adapter.setDataList(options)
        }
    }

    private fun bindBackground() {
        customChatLayout?.background = bg
    }

    private fun getOptionListViewModelList(points: List<CsatAttributesPojo.Csat.Point>?): ArrayList<ChatOptionListUiModel> {
        val list = arrayListOf<ChatOptionListUiModel>()
        points?.forEach {
            val option = ChatOptionListUiModel()
            option.apply {
                text = it.caption ?: ""
                value = it.score
                type = OPTION_TYPE_CSAT
            }
            list.add(option)
        }
        return list
    }

    private fun onOptionListSelected(): (ChatOptionListUiModel) -> Unit = {
        csatOptionListListener.csatOptionListSelected(it, model)
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
        val LAYOUT = R.layout.item_chatbot_csat_option
    }
}
