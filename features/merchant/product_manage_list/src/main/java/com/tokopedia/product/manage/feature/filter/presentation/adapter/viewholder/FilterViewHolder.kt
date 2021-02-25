package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.presentation.adapter.decorator.SpacingItemDecoration
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.*
import kotlinx.android.synthetic.main.item_filter.view.*
import kotlinx.android.synthetic.main.partial_layout_chips.view.*
import kotlinx.android.synthetic.main.widget_header.view.*


class FilterViewHolder(view: View,
                       private val seeAllListener: SeeAllListener,
                       private val chipClickListener: ChipsAdapter.ChipClickListener,
                       private val showChipsListener: ShowChipsListener) : AbstractViewHolder<FilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_filter
        private const val NO_ROTATION = 0f
        private const val FLIPPED_ROTATION = 180f
    }

    private val recyclerView: RecyclerView = itemView.chipsRecyclerView
    private var adapter: ChipsAdapter? = null
    private val headerWidget: HeaderWidget = itemView.filterHeader
    private val seeAllWidget: SeeAllWidget = itemView.filterSeeAll


    init {
        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
        recyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
        recyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
    }

    override fun bind(element: FilterUiModel) {
        headerWidget.bind(element.title)
        setChipsShown(element.isChipsShown)
        headerWidget.setOnClickListener {
            showChipsListener.onShowChips(element)
        }
        headerWidget.arrow.setOnClickListener {
            showChipsListener.onShowChips(element)
        }
        initAdapter(element)
        seeAllWidget.setOnClickListener {
            seeAllListener.onSeeAll(element)
        }
    }

    private fun setChipsShown(isChipsShown: Boolean) {
        if(isChipsShown) {
            showChips()
        } else {
            hideChips()
        }
    }

    private fun initAdapter(element: FilterUiModel) {
        adapter = when(element.title) {
            ProductManageFilterMapper.SORT_HEADER -> {
                ChipsAdapter(chipClickListener, false, element.title)
            }
            ProductManageFilterMapper.ETALASE_HEADER -> {
                ChipsAdapter(chipClickListener, false, element.title)
            }
            else -> ChipsAdapter(chipClickListener, true, element.title)
        }
        recyclerView.adapter = adapter
        adapter?.setData(element)
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