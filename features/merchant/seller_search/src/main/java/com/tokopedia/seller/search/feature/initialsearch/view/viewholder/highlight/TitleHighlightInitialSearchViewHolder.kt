package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.databinding.ItemTitleHeaderHighlightSearchBinding
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightInitialSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TitleHighlightInitialSearchViewHolder(view: View) :
    AbstractViewHolder<ItemTitleHighlightInitialSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_header_highlight_search
    }

    private val binding: ItemTitleHeaderHighlightSearchBinding? by viewBinding()

    override fun bind(element: ItemTitleHighlightInitialSearchUiModel?) {
        binding?.run {
            tvHighlightSearch.text = element?.title.orEmpty()
        }
    }
}