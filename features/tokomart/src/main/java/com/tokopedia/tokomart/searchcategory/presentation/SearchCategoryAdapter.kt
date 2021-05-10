package com.tokopedia.tokomart.searchcategory.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R

open class SearchCategoryAdapter(
        private val typeFactory: BaseSearchCategoryTypeFactory
): ListAdapter<Visitable<*>, AbstractViewHolder<*>>(SearchCategoryDiffUtil()) {

    protected open val notFullSpanLayout = listOf(R.layout.item_tokomart_search_category_product)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

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

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        if (position !in currentList.indices) return

        @Suppress("UNCHECKED_CAST")
        (holder as AbstractViewHolder<Visitable<*>>).bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        val item = getItem(position) as Visitable<BaseSearchCategoryTypeFactory>

        return item.type(typeFactory)
    }
}
