package com.tokopedia.product.manage.feature.list.view.ui.tab

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.feature.list.view.adapter.ProductFilterAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductFilterItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterTabViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterTabViewHolder.*
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.*

class ProductManageTabFilter: RecyclerView {

    companion object {
        const val TAB_MORE_FILTER_POSITION = 0
    }

    var selectedFilter: FilterTabViewModel? = null
        private set

    private val tabFilterAdapter by lazy { adapter as? ProductFilterAdapter }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    fun init(listener: ProductFilterListener) {
        adapter = ProductFilterAdapter(listener)
        addItemDecoration(ProductFilterItemDecoration())
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        isNestedScrollingEnabled = false
    }

    fun resetAllFilter(selectedFilter: FilterTabViewHolder? = null) {
        for(i in 0..tabFilterAdapter?.itemCount.orZero()) {
            val viewHolder = findViewHolderForAdapterPosition(i) as? FilterTabViewHolder
            if(viewHolder != selectedFilter) viewHolder?.resetFilter()
        }
    }

    fun setData(filters: List<FilterTabViewModel>) {
        tabFilterAdapter?.clearAllElements()
        tabFilterAdapter?.addElement(filters)
    }

    fun getProductCount(): Int {
        return tabFilterAdapter?.data?.firstOrNull {
                it.status == selectedFilter?.status
            }?.count.orZero()
    }

    fun setFilterCount(filterCount: Int) {
        tabFilterAdapter?.list?.set(TAB_MORE_FILTER_POSITION, MoreFilter(filterCount))
        tabFilterAdapter?.notifyItemChanged(TAB_MORE_FILTER_POSITION)
    }

    fun setSelectedFilter(selectedFilter: FilterTabViewModel) {
        this.selectedFilter = selectedFilter
    }

    fun resetSelectedFilter() {
        selectedFilter = null
    }

    fun resetFilters() {
        resetSelectedFilter()
        resetAllFilter()
        setFilterCount(0)
    }

    fun isActive() = selectedFilter != null
}