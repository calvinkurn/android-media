package com.tokopedia.review.feature.reviewlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.review.feature.reviewlist.view.viewholder.ReviewSellerLoadingViewHolder
import com.tokopedia.review.feature.reviewlist.view.viewholder.ReviewSummaryViewHolder
import com.tokopedia.review.feature.reviewlist.view.viewholder.SellerReviewListViewHolder

open class SellerReviewListTypeFactory(
    private val reviewSummaryListener: ReviewSummaryViewHolder.ReviewSummaryViewListener,
    protected val sellerReviewListener: SellerReviewListViewHolder.SellerReviewListListener
) : BaseAdapterTypeFactory(), TypeFactoryViewHolder {

    override fun type(productRatingOverallUiModel: ProductRatingOverallUiModel): Int {
        return ReviewSummaryViewHolder.LAYOUT_RES
    }

    override fun type(productReviewUiModel: ProductReviewUiModel): Int {
        return SellerReviewListViewHolder.LAYOUT_RES
    }

    override fun type(viewModel: LoadingModel): Int {
        return ReviewSellerLoadingViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ReviewSummaryViewHolder.LAYOUT_RES -> ReviewSummaryViewHolder(parent, reviewSummaryListener)
            SellerReviewListViewHolder.LAYOUT_RES -> SellerReviewListViewHolder(parent, sellerReviewListener)
            ReviewSellerLoadingViewHolder.LAYOUT_RES -> ReviewSellerLoadingViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
