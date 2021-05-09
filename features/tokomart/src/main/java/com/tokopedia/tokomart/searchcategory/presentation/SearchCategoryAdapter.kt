package com.tokopedia.tokomart.searchcategory.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

open class SearchCategoryAdapter(
        private val typeFactory: BaseSearchCategoryTypeFactory
): ListAdapter<Visitable<*>, AbstractViewHolder<*>>(SearchCategoryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
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
