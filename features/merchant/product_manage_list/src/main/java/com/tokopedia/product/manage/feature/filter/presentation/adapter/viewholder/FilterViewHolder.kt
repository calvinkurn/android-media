package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.decorator.SpacingItemDecoration
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.*
import com.tokopedia.product.manage.oldlist.R
import kotlinx.android.synthetic.main.widget_header.view.*

class FilterViewHolder(view: View, private val seeAllListener: SeeAllListener) : AbstractViewHolder<FilterViewModel>(view), ItemClickListener {

    companion object {
        val LAYOUT = R.layout.item_filter
    }

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.chips_recycler_view)
    private val adapter: ChipsAdapter
    private val headerWidget: HeaderWidget = itemView.findViewById(R.id.filter_header)
    private val seeAllWidget: SeeAllWidget = itemView.findViewById(R.id.filter_see_all)


    init {
        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8)
        recyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
        recyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = ChipsAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun bind(element: FilterViewModel) {
        headerWidget.bind(element.title)
        headerWidget.arrow.setOnClickListener {

        }
        adapter.setData(element.names)
        seeAllWidget.setOnClickListener {
            seeAllListener.onSeeAll(element)
        }
    }

    override fun onItemClicked() {

    }
}