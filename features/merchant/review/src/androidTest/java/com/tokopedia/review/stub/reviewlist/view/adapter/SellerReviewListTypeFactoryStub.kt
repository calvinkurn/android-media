package com.tokopedia.review.stub.reviewlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.reviewlist.view.adapter.SellerReviewListTypeFactory
import com.tokopedia.review.feature.reviewlist.view.viewholder.ReviewSummaryViewHolder
import com.tokopedia.review.feature.reviewlist.view.viewholder.SellerReviewListViewHolder
import com.tokopedia.review.stub.reviewlist.view.viewholder.SellerReviewListViewHolderStub

class SellerReviewListTypeFactoryStub(
    reviewSummaryListener: ReviewSummaryViewHolder.ReviewSummaryViewListener,
    sellerReviewListener: SellerReviewListViewHolder.SellerReviewListListener
) : SellerReviewListTypeFactory(reviewSummaryListener, sellerReviewListener) {
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SellerReviewListViewHolder.LAYOUT_RES -> SellerReviewListViewHolderStub(
                parent, sellerReviewListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
