package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SearchInTokopediaDataView
import com.tokopedia.search.result.presentation.view.listener.SearchInTokopediaListener
import kotlinx.android.synthetic.main.search_result_global_search_view_holder.view.*

class SearchInTokopediaViewHolder(
        itemView: View,
        private val searchInTokopediaListener: SearchInTokopediaListener
) : AbstractViewHolder<SearchInTokopediaDataView>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_global_search_view_holder
    }

    override fun bind(element: SearchInTokopediaDataView?) {
        if (element == null) return

        itemView.searchResultGlobalSearchInTokopediaButton?.setOnClickListener {
            searchInTokopediaListener.onSearchInTokopediaClick(element.applink)
        }
    }
}