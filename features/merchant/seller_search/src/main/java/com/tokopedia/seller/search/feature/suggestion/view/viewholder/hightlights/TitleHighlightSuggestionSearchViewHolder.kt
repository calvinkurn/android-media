package com.tokopedia.seller.search.feature.suggestion.view.viewholder.hightlights

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.databinding.ItemTitleHeaderHighlightSearchBinding
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemTitleHighlightSuggestionSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TitleHighlightSuggestionSearchViewHolder(view: View): AbstractViewHolder<ItemTitleHighlightSuggestionSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_header_highlight_search
    }

    private val binding: ItemTitleHeaderHighlightSearchBinding? by viewBinding()

    override fun bind(element: ItemTitleHighlightSuggestionSearchUiModel?) {
        binding?.run {
            tvHighlightSearch.text = element?.title.orEmpty()
        }
    }
}