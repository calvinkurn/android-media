package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.decorator.SpacingItemDecoration
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.*
import kotlinx.android.synthetic.main.widget_header.view.*
import android.R.attr.button
import androidx.core.view.ViewCompat.getRotation



class FilterViewHolder(view: View, private val seeAllListener: SeeAllListener, chipClickListener: ChipClickListener) : AbstractViewHolder<FilterViewModel>(view) {

    companion object {
        val LAYOUT = com.tokopedia.product.manage.R.layout.item_filter
    }

    private val recyclerView: RecyclerView = itemView.findViewById(com.tokopedia.product.manage.R.id.chips_recycler_view)
    val adapter: ChipsAdapter
    private val headerWidget: HeaderWidget = itemView.findViewById(com.tokopedia.product.manage.R.id.filter_header)
    private val seeAllWidget: SeeAllWidget = itemView.findViewById(com.tokopedia.product.manage.R.id.filter_see_all)
    private var isChipsShown = false


    init {
        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8)
        recyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
        recyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = ChipsAdapter(chipClickListener)
        recyclerView.adapter = adapter
    }

    override fun bind(element: FilterViewModel) {
        headerWidget.bind(element.title)
        isChipsShown = element.isChipsShown
        if(!element.isChipsShown) {
            hideChips()
        }
        headerWidget.arrow.setOnClickListener {
            toggleChipsVisibility()
        }
        adapter.setData(element.names, element.selectData)
        seeAllWidget.setOnClickListener {
            seeAllListener.onSeeAll(element)
        }
    }

    private fun hideChips() {
        recyclerView.visibility = View.GONE
        seeAllWidget.visibility = View.GONE
        val deg = if (headerWidget.arrow.rotation == 180f) 0f else 180f
        headerWidget.arrow.rotation = deg
    }

    private fun showChips() {
        recyclerView.visibility = View.VISIBLE
        seeAllWidget.visibility = View.VISIBLE
        val deg = if (headerWidget.arrow.rotation == 180f) 0f else 180f
        headerWidget.arrow.rotation = deg
    }

    private fun toggleChipsVisibility() {
        isChipsShown = !isChipsShown
        if(isChipsShown) {
            showChips()
        } else {
            hideChips()
        }
    }
}