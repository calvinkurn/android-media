package com.tokopedia.tokomart.searchcategory.presentation

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.base.adapter.BaseTokoMartListAdapter

open class SearchCategoryAdapter(
        private val typeFactory: BaseSearchCategoryTypeFactory
): BaseTokoMartListAdapter<Visitable<*>, BaseSearchCategoryTypeFactory>(typeFactory, SearchCategoryDiffUtil()) {

    protected open val notFullSpanLayout = listOf(R.layout.item_tokomart_search_category_product)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)

        setFullSpan(view, viewType)

        return typeFactory.createViewHolder(view, viewType)
    }

    protected open fun setFullSpan(view: View, viewType: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams !is StaggeredGridLayoutManager.LayoutParams) return
        layoutParams.isFullSpan = isFullSpan(viewType)
    }

    protected open fun isFullSpan(viewType: Int): Boolean {
        return !notFullSpanLayout.contains(viewType)
    }
}
