package com.tokopedia.product.manage.feature.list.view.ui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductFilterItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterViewHolder
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel

class ProductManageTabFilter: RecyclerView {

    var selectedFilter: FilterViewModel? = null
        private set

    init {
        addItemDecoration(ProductFilterItemDecoration())
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        isNestedScrollingEnabled = false
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    fun setSelectedFilter(selectedFilter: FilterViewModel) {
        this.selectedFilter = selectedFilter
    }

    fun resetSelectedFilter() {
        selectedFilter = null
    }

    fun isActive(): Boolean {
        return selectedFilter != null
    }

    fun resetAllFilter(selectedFilter: FilterViewHolder) {
        for(i in 0..adapter?.itemCount.orZero()) {
            val viewHolder = findViewHolderForAdapterPosition(i) as? FilterViewHolder
            if(viewHolder != selectedFilter) viewHolder?.resetFilter()
        }
    }
}