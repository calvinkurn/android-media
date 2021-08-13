package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_see_more_recent_search.view.*

class RecentSearchSeeMoreViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<RecentSearchSeeMoreDataView>(itemView) {

    private fun bindTitle() {
        itemView.autocompleteSeeMoreButton?.text = getString(R.string.autocomplete_see_more_recent_search)
    }

    override fun bind(element: RecentSearchSeeMoreDataView) {
        bindTitle()
        bindListener(element)
    }

    private fun bindListener(item: RecentSearchSeeMoreDataView) {
        itemView.autocompleteSeeMoreButton?.setOnClickListener { clickListener.onRecentSearchSeeMoreClicked() }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_see_more_recent_search
    }
}