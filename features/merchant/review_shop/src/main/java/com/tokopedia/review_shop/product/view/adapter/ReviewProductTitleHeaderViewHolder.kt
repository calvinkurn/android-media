package com.tokopedia.review_shop.product.view.adapter

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review_shop.R

/**
 * Created by zulfikarrahman on 1/16/18.
 */
class ReviewProductTitleHeaderViewHolder(view: View) : AbstractViewHolder<ReviewProductModelTitleHeader?>(view) {
    private val title: TextView
    override fun bind(element: ReviewProductModelTitleHeader?) {
        title.text = element?.title.orEmpty()
    }

    companion object {
        val LAYOUT = R.layout.item_product_review_header_shop_page
    }

    init {
        title = view.findViewById(R.id.title_text)
    }
}