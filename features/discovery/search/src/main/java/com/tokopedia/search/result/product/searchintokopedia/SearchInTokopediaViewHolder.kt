package com.tokopedia.search.result.product.searchintokopedia

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultGlobalSearchViewHolderBinding
import com.tokopedia.utils.view.binding.viewBinding

class SearchInTokopediaViewHolder(
        itemView: View,
        private val searchInTokopediaListener: SearchInTokopediaListener
) : AbstractViewHolder<SearchInTokopediaDataView>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_global_search_view_holder
    }
    private var binding: SearchResultGlobalSearchViewHolderBinding? by viewBinding()

    override fun bind(element: SearchInTokopediaDataView?) {
        val binding = binding?: return
        if (element == null) return

        binding.searchResultGlobalSearchInTokopediaButton.setOnClickListener {
            searchInTokopediaListener.onSearchInTokopediaClick(element.applink)
        }
    }
}