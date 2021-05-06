package com.tokopedia.tokomart.searchcategory.presentation

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

open class SearchCategoryDiffUtil: DiffUtil.ItemCallback<Visitable<*>>() {

    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return true
    }
}