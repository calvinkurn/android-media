package com.tokopedia.topchat.chatroom.view.adapter.viewholder.filter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.ChatFilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class ChatFilterViewHolder(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<ChatFilterUiModel>(itemView) {

    private val filter: ChipsUnify? = itemView?.findViewById(R.id.chips_filter)

    interface Listener {
        fun onFilterClicked(element: ChatFilterUiModel)
        fun isSelected(element: ChatFilterUiModel): Boolean
    }

    override fun bind(element: ChatFilterUiModel?, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        toggleChipState()
    }

    override fun bind(element: ChatFilterUiModel) {
        bindFilterText(element)
        bindClick(element)
        bindState(element)
    }

    private fun bindFilterText(element: ChatFilterUiModel) {
        filter?.chipText = itemView.context.getString(element.tagName)
    }

    private fun bindClick(element: ChatFilterUiModel) {
        filter?.setOnClickListener {
            listener?.onFilterClicked(element)
            toggleChipState()
        }
    }

    private fun bindState(element: ChatFilterUiModel) {
        val isSelected = listener?.isSelected(element) ?: false
        val state = if (isSelected) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
        filter?.chipType = state
    }

    private fun toggleChipState() {
        val state = when (filter?.chipType) {
            ChipsUnify.TYPE_NORMAL -> ChipsUnify.TYPE_SELECTED
            ChipsUnify.TYPE_SELECTED -> ChipsUnify.TYPE_NORMAL
            else -> null
        }
        state?.let {
            filter?.chipType = it
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_filter_chip
    }
}