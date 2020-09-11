package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_see_more_recent_search.view.*

class RecentSearchSeeMoreViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<RecentSearchSeeMoreViewModel>(itemView) {

    override fun bind(element: RecentSearchSeeMoreViewModel?, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return
        element?.let{
            bindTitle(it)
        }
    }

    private fun bindTitle(item: RecentSearchSeeMoreViewModel) {
        if (item.seeMore) {
            itemView.autocompleteSeeMoreButton?.text = getString(R.string.autocomplete_see_more_recent_search)
        } else {
            itemView.autocompleteSeeMoreButton?.text = getString(R.string.autocomplete_see_less_recent_search)
        }
    }

    override fun bind(element: RecentSearchSeeMoreViewModel) {
        bindTitle(element)
        bindListener(element)
    }

    private fun bindListener(item: RecentSearchSeeMoreViewModel) {
        itemView.autocompleteSeeMoreButton?.setOnClickListener { clickListener.onRecentSearchSeeMoreClicked(item) }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_see_more_recent_search
    }
}