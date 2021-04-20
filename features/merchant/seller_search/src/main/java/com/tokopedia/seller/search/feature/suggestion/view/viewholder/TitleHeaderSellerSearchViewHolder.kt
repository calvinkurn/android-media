package com.tokopedia.seller.search.feature.suggestion.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHeaderSellerSearchUiModel
import kotlinx.android.synthetic.main.item_title_header_seller_search.view.*

class TitleHeaderSellerSearchViewHolder(view: View): AbstractViewHolder<TitleHeaderSellerSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_header_seller_search
    }

    override fun bind(element: TitleHeaderSellerSearchUiModel?) {
        with(itemView) {
            tvTitleHeaderSellerSearch.text = element?.title
        }
    }
}