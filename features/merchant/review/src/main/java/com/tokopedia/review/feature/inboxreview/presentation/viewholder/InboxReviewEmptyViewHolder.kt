package com.tokopedia.review.feature.inboxreview.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewEmptyUiModel
import kotlinx.android.synthetic.main.item_empty_state_list_rating_product.view.*

class InboxReviewEmptyViewHolder(view: View): AbstractViewHolder<InboxReviewEmptyUiModel>(view) {

    companion object {
        const val EMPTY_STATE_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/empty_review_inbox_seller.png"
        val LAYOUT = R.layout.item_empty_state_list_rating_product
    }

    override fun bind(element: InboxReviewEmptyUiModel) {
        with(itemView) {
            icEmptyStateRatingProduct.loadImage(EMPTY_STATE_IMAGE_URL)
            if(element.isFilter) {
                tvContentNoReviewsYet.text = getString(R.string.empty_state_message_wrong_filter).orEmpty()
            } else {
                tvContentNoReviewsYet.text = getString(R.string.no_reviews_yet_content_detail).orEmpty()
            }
        }
    }
}