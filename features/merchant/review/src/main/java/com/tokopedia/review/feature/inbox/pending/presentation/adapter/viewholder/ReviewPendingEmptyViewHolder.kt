package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import kotlinx.android.synthetic.main.item_review_pending_empty.view.*

class ReviewPendingEmptyViewHolder(view: View) : AbstractViewHolder<ReviewPendingEmptyUiModel>(view) {

    companion object {
        const val REVIEW_PENDING_NO_PRODUCTS_BOUGHT_IMAGE = "https://ecs7.tokopedia.net/android/others/review_inbox_no_products.png"
        val LAYOUT = R.layout.item_review_pending_empty
    }

    override fun bind(element: ReviewPendingEmptyUiModel) {
        itemView.apply {
            reviewPendingEmptyImage.loadImage(REVIEW_PENDING_NO_PRODUCTS_BOUGHT_IMAGE)
            reviewPendingEmptyTitle.text = getString(R.string.review_pending_no_product_empty_title)
            reviewPendingEmptySubtitle.text = getString(R.string.review_pending_no_product_empty_content)
        }
    }
}