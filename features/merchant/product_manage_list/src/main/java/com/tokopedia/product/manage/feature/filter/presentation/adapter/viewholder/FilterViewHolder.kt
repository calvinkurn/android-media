package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.presentation.adapter.decorator.SpacingItemDecoration
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.*
import kotlinx.android.synthetic.main.widget_header.view.*


class FilterViewHolder(view: View,
                       private val seeAllListener: SeeAllListener,
                       private val chipClickListener: ChipClickListener,
                       private val showChipsListener: ShowChipsListener) : AbstractViewHolder<FilterViewModel>(view) {

    companion object {
        val LAYOUT = com.tokopedia.product.manage.R.layout.item_filter
        private const val NO_ROTATION = 0f
        private const val FLIPPED_ROTATION = 180f
    }

    private val recyclerView: RecyclerView = itemView.findViewById(com.tokopedia.product.manage.R.id.chips_recycler_view)
    private var adapter: ChipsAdapter? = null
    private val headerWidget: HeaderWidget = itemView.findViewById(com.tokopedia.product.manage.R.id.filter_header)
    private val seeAllWidget: SeeAllWidget = itemView.findViewById(com.tokopedia.product.manage.R.id.filter_see_all)


    init {
        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8)
        recyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
        recyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
    }

    override fun bind(element: FilterViewModel) {
        headerWidget.bind(element.title)
        if(!element.isChipsShown) {
            hideChips()
        } else {
            showChips()
        }
        headerWidget.arrow.setOnClickListener {
            showChipsListener.onShowChips(element)
        }
        adapter = if(element.title == ProductManageFilterMapper.SORT_HEADER ||
                element.title == ProductManageFilterMapper.ETALASE_HEADER) {
            ChipsAdapter(chipClickListener, false)
        } else {
            ChipsAdapter(chipClickListener, true)
        }
        recyclerView.adapter = adapter
        adapter?.setData(element)
        seeAllWidget.setOnClickListener {
            seeAllListener.onSeeAll(element)
        }
    }

    private fun hideChips() {
        recyclerView.visibility = View.GONE
        seeAllWidget.visibility = View.GONE
        headerWidget.arrow.rotation = NO_ROTATION
    }

    private fun showChips() {
        recyclerView.visibility = View.VISIBLE
        seeAllWidget.visibility = View.VISIBLE
        headerWidget.arrow.rotation = FLIPPED_ROTATION
    }
}