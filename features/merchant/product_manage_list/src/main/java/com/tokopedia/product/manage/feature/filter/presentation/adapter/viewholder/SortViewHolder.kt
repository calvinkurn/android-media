package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SortViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter
import com.tokopedia.product.manage.oldlist.R

class SortViewHolder(view: View, itemClickListener: ItemClickListener) : AbstractViewHolder<SortViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.layout_chips
    }

    private val clickListener: ItemClickListener = itemClickListener
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.filter_recycler_view)
    private val adapter: ChipsAdapter


    init {
        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        recyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = ChipsAdapter(clickListener)
        recyclerView.adapter = adapter
    }

    override fun bind(element: SortViewModel) {
        adapter.setData(element.names)
    }

}

interface ItemClickListener {
    fun onItemClicked()
}

