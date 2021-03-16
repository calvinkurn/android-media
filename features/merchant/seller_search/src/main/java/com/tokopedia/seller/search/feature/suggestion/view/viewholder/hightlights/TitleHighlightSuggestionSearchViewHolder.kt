package com.tokopedia.seller.search.feature.suggestion.view.viewholder.hightlights

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemTitleHighlightSuggestionSearchUiModel
import kotlinx.android.synthetic.main.item_title_header_highlight_search.view.*

class TitleHighlightSuggestionSearchViewHolder(view: View): AbstractViewHolder<ItemTitleHighlightSuggestionSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_header_highlight_search
    }

    override fun bind(element: ItemTitleHighlightSuggestionSearchUiModel?) {
        with(itemView) {
            tvHighlightSearch?.text = element?.title.orEmpty()
        }
    }
}