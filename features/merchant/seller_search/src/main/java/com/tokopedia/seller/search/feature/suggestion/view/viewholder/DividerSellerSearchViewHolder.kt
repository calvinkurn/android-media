package com.tokopedia.seller.search.feature.suggestion.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.DividerSellerSearchUiModel
import kotlinx.android.synthetic.main.item_divider_seller_search.view.*

class DividerSellerSearchViewHolder(view: View): AbstractViewHolder<DividerSellerSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_divider_seller_search
    }

    override fun bind(element: DividerSellerSearchUiModel) {
        with(itemView) {
            if (element.isVisible) {
                dividerSellerSearch?.show()
            } else {
                dividerSellerSearch?.hide()
            }
        }
    }
}