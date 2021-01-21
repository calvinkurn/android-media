package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightSearchUiModel
import kotlinx.android.synthetic.main.item_title_header_highlight_search.view.*

class TitleHighlightSearchViewHolder(view: View): AbstractViewHolder<ItemTitleHighlightSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_header_highlight_search
    }

    override fun bind(element: ItemTitleHighlightSearchUiModel?) {
        with(itemView) {
            tvHighlightSearch?.text = element?.title.orEmpty()
        }
    }
}