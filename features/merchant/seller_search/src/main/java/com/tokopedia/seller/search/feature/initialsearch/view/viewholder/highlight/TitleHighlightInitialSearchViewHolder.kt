package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightInitialSearchUiModel
import kotlinx.android.synthetic.main.item_title_header_highlight_search.view.*

class TitleHighlightInitialSearchViewHolder(view: View): AbstractViewHolder<ItemTitleHighlightInitialSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_header_highlight_search
    }

    override fun bind(element: ItemTitleHighlightInitialSearchUiModel?) {
        with(itemView) {
            tvHighlightSearch?.text = element?.title.orEmpty()
        }
    }
}