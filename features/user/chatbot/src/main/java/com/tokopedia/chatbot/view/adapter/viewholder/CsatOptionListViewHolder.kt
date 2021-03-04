package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.chatbot.domain.pojo.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.util.OptionListRecyclerItemDecorator
import com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist.ChatOptionListAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist.OPTION_TYPE_CSAT
import com.tokopedia.chatbot.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify

class CsatOptionListViewHolder(itemView: View,
                               private val csatOptionListListener: CsatOptionListListener
) : BaseChatViewHolder<CsatOptionsViewModel>(itemView) {

    private val adapter: ChatOptionListAdapter
    private var model: CsatOptionsViewModel? = null
    private val chatActionListSelection: RecyclerView = itemView.findViewById<RecyclerView>(R.id.chat_csat_option_list_selection)
    private val chatActionBubbleSelectionContainer: CardUnify = itemView.findViewById<CardUnify>(R.id.chat_csat_option_list_container)
    private val mesage: TextView = itemView.findViewById<TextView>(R.id.message)


    init {
        ViewCompat.setNestedScrollingEnabled(chatActionListSelection, false)
        adapter = ChatOptionListAdapter(onOptionListSelected())
        chatActionListSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.VERTICAL, false)
        chatActionListSelection.adapter = adapter
        chatActionListSelection.addItemDecoration(OptionListRecyclerItemDecorator(itemView.context))

    }

    override fun bind(viewModel: CsatOptionsViewModel?) {
        super.bind(viewModel)
        model = viewModel
        mesage.text = viewModel?.message
        if (viewModel?.isSubmited == true) {
            chatActionBubbleSelectionContainer.hide()
        } else {
            chatActionBubbleSelectionContainer.show()
            val options = getOptionListViewModelList(viewModel?.csat?.points)
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
        val LAYOUT = R.layout.chatbot_csat_option_layout
    }
}
