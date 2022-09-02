package com.tokopedia.topchat.chatroom.view.adapter

import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.ChatFilterTypeFactory
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.filter.ChatFilterViewHolder
import com.tokopedia.topchat.chatroom.view.custom.ChatFilterView
import com.tokopedia.topchat.chatroom.view.uimodel.ChatFilterUiModel

class ChatFilterAdapter constructor(
        private val typeFactory: ChatFilterTypeFactory
) : BaseListAdapter<Visitable<*>, ChatFilterTypeFactory>(typeFactory),
        ChatFilterViewHolder.Listener {

    var filterListener: ChatFilterView.FilterListener? = null

    private val defaultFilter = ChatFilterUiModel()
    private var selectedFilter = defaultFilter
    private val handler = Handler()

    fun reset() {
        selectedFilter = defaultFilter
    }

    fun showFilter(data: List<ChatFilterUiModel>) {
        visitables.clear()
        visitables.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return typeFactory.onCreateViewHolder(parent, viewType, this)
    }

    override fun onFilterClicked(element: ChatFilterUiModel) {
        deselectOtherSelectedFilter(element)
        notifyFilterChanged(element)
    }

    override fun isSelected(element: ChatFilterUiModel): Boolean {
        return selectedFilter == element
    }

    private fun deselectOtherSelectedFilter(element: ChatFilterUiModel) {
        if (isSelected(element)) return
        val previouslySelectedFilterPosition = visitables.indexOf(selectedFilter)
        if (previouslySelectedFilterPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(previouslySelectedFilterPosition, Any())
        }
    }

    private fun notifyFilterChanged(element: ChatFilterUiModel) {
        if (isSelected(element)) {
            selectedFilter = defaultFilter
        } else {
            selectedFilter = element
        }
        filterListener?.onFilterChanged(selectedFilter.id)
    }

    fun hasTopBotFilter(): Boolean {
        return visitables.size > 2
    }

    fun addTopBotFilter(topBotFilter: ChatFilterUiModel) {
        handler.post {
            val index= visitables.size
            visitables.add(topBotFilter)
            notifyItemInserted(index)
        }
    }

}