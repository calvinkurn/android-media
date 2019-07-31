package com.tokopedia.navigation.presentation.adapter.viewholder

import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateFilterSectionAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.navigation.presentation.view.listener.NotificationSectionFilterListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel


/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFilterItemViewHolder(itemView: View, var listener: NotificationSectionFilterListener)
    : AbstractViewHolder<NotificationUpdateFilterItemViewModel>(itemView),
        ItemSectionListener{

    private var selectedPosition = -1

    override fun itemSectionPicked(id: Int, filterType: String) {
        val oldPosition = selectedPosition
        selectedPosition = if(id == selectedPosition){
            -1
        }else {
            id
        }
        val newPosition = selectedPosition

        val adapter = listFilterItemSectionRecyclerView.adapter as NotificationUpdateFilterSectionAdapter
        adapter?.let {
            changeSelected(oldPosition, it, false)
            changeSelected(newPosition, it, true)
        }
        listener.sectionPicked(adapterPosition, newPosition)
    }

    private fun changeSelected(position: Int, adapter: NotificationUpdateFilterSectionAdapter,
                               selected: Boolean) {
        val list = adapter.list
        if (position < 0 || position > list.size) return
        (adapter.list[position] as NotificationUpdateFilterSectionItemViewModel).selected = selected
        adapter.notifyItemChanged(position)
    }


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_filter_section
    }


    private val title: TextView
    private val listFilterItemSectionRecyclerView: RecyclerView

    init {
        title = itemView.findViewById(R.id.title_section)
        listFilterItemSectionRecyclerView = itemView.findViewById(R.id.item_section_list)
    }

    override fun bind(element: NotificationUpdateFilterItemViewModel) {
        title.text = element.title
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(listFilterItemSectionRecyclerView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        listFilterItemSectionRecyclerView.let {
            val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_8)
            it.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
            it.layoutManager = chipsLayoutManager
            val filterSectionItemAdapter =
                    NotificationUpdateFilterSectionAdapter(
                            NotificationUpdateFilterSectionTypeFactoryImpl(this, element.filterType))
            it.adapter = filterSectionItemAdapter
            filterSectionItemAdapter.addElement(element.list)
        }
    }

    override fun bind(element: NotificationUpdateFilterItemViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)

        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }
        selectedPosition = -1
        bindSelectedView(payloads[0] as Int)
    }

    private fun bindSelectedView(index: Int) {
        listFilterItemSectionRecyclerView.adapter?.notifyItemChanged(index)
    }


    class SpacingItemDecoration(private val dimen: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = this.dimen
            outRect.top = this.dimen / 2
            outRect.bottom = this.dimen / 2
        }
    }
}

interface ItemSectionListener {
    fun itemSectionPicked(id: Int, filterType: String)
}