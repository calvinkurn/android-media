package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateImpressionListener
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_recyclerview_autocomplete.view.*

class RecentSearchViewHolder(
        itemView: View,
        clickListener: InitialStateItemClickListener,
        private val impressionListener: InitialStateImpressionListener
): AbstractViewHolder<RecentSearchViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_recent_search_autocomplete
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
        if (element.list.isNotEmpty()) impressionListener.onRecentSearchImpressed(element.list)
    }
}