package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutSeeMoreRecentSearchBinding
import com.tokopedia.utils.view.binding.viewBinding

class RecentSearchSeeMoreViewHolder(
    itemView: View,
    private val listener: RecentSearchListener,
) : AbstractViewHolder<RecentSearchSeeMoreDataView>(itemView) {

    private var binding: LayoutSeeMoreRecentSearchBinding? by viewBinding()

    private fun bindTitle() {
        binding?.autocompleteSeeMoreButton?.text =
            getString(R.string.autocomplete_see_more_recent_search)
    }

    override fun bind(element: RecentSearchSeeMoreDataView) {
        bindTitle()
        bindListener(element)
    }

    private fun bindListener(item: RecentSearchSeeMoreDataView) {
        binding?.autocompleteSeeMoreButton?.setOnClickListener {
            listener.onRecentSearchSeeMoreClicked()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_see_more_recent_search
    }
}