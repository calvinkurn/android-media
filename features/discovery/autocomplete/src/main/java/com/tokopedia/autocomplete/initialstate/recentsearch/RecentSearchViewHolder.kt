package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.adapter.ItemClickListener
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.newfiles.BaseItemInitialStateSearch
import kotlinx.android.synthetic.main.layout_recent_item_autocomplete.view.*
import kotlinx.android.synthetic.main.layout_recyclerview_autocomplete.view.*

class RecentSearchViewHolder(
        itemView: View,
        clickListener: ItemClickListener
): AbstractViewHolder<RecentSearchViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_recentsearch_autocomplete
    }

    private val adapter: RecentSearchItemAdapter

    init {
        val layoutManager = LinearLayoutManager(itemView.context)
        itemView.recyclerView?.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(itemView.recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = RecentSearchItemAdapter(clickListener)
        itemView.recyclerView?.adapter = adapter
    }

    override fun bind(element: RecentSearchViewModel) {
        adapter.setData(element.list)
    }
}