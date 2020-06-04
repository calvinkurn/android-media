package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.View
import com.example.review.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.feature.inbox.pending.data.ReviewPendingEmptyState
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import kotlinx.android.synthetic.main.item_review_pending_empty.view.*

class ReviewPendingEmptyViewHolder(view: View) : AbstractViewHolder<ReviewPendingEmptyUiModel>(view) {

    companion object {
        const val REVIEW_PENDING_NO_PRODUCTS_BOUGHT_IMAGE = "https://ecs7.tokopedia.net/android/others/review_inbox_no_products.png"
        const val REVIEW_PENDING_NO_PRODUCTS_SEARCH_RESULT_IMAGE = "https://ecs7.tokopedia.net/android/others/review_inbox_search_empty.png"
        val LAYOUT = R.layout.item_review_pending_empty
    }

    override fun bind(element: ReviewPendingEmptyUiModel) {
        when(element.emptyState) {
            is ReviewPendingEmptyState.ReviewPendingNoProductsBought -> {
                setEmptyNoProductsView()
            }
            is ReviewPendingEmptyState.ReviewPendingNoProductsSearchResult -> {
                setEmptyNoProductsSearchResultView()
            }
        }
    }

    private fun setEmptyNoProductsView() {
        itemView.apply {
            reviewPendingEmptyImage.loadImage(REVIEW_PENDING_NO_PRODUCTS_BOUGHT_IMAGE)
            reviewPendingEmptyTitle.text = getString(R.string.review_pending_no_product_empty_title)
            reviewPendingEmptySubtitle.text = getString(R.string.review_pending_no_product_empty_content)
        }
    }

    private fun setEmptyNoProductsSearchResultView() {
        itemView.apply {
            reviewPendingEmptyImage.loadImage(REVIEW_PENDING_NO_PRODUCTS_SEARCH_RESULT_IMAGE)
            reviewPendingEmptyTitle.text = getString(R.string.review_pending_no_product_search_result_title)
            reviewPendingEmptySubtitle.text = getString(R.string.review_pending_no_product_search_content)
        }
    }
}