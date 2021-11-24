package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutRecentSearchAutocompleteBinding
import com.tokopedia.utils.view.binding.viewBinding

class RecentSearchViewHolder(
    itemView: View,
    listener: RecentSearchListener
): AbstractViewHolder<RecentSearchDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_recent_search_autocomplete
    }

    private val adapter: RecentSearchItemAdapter = RecentSearchItemAdapter(listener)

    private var binding: LayoutRecentSearchAutocompleteBinding? by viewBinding()

    init {
        binding?.recyclerViewRecentSearch?.let { recyclerView ->
            val layoutManager = LinearLayoutManager(itemView.context)
            recyclerView.layoutManager = layoutManager
            ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
            recyclerView.adapter = adapter
        }
    }

    override fun bind(element: RecentSearchDataView) {
        adapter.setData(element.list)
    }
}