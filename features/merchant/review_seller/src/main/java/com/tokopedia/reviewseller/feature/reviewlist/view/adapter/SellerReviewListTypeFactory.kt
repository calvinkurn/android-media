package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.FilterAndSortViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.ReviewSummaryViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.SellerReviewListViewHolder

class SellerReviewListTypeFactory: BaseAdapterTypeFactory(), TypeFactoryViewHolder {

    override fun type(productRatingOverallModel: ProductRatingOverallModel): Int {
        return ReviewSummaryViewHolder.LAYOUT_RES
    }

    override fun type(productReviewUiModel: ProductReviewUiModel): Int {
        return SellerReviewListViewHolder.LAYOUT_RES
    }

    override fun type(filterAndSortModel: FilterAndSortModel): Int {
        return FilterAndSortViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ReviewSummaryViewHolder.LAYOUT_RES -> ReviewSummaryViewHolder(parent)
            SellerReviewListViewHolder.LAYOUT_RES -> SellerReviewListViewHolder(parent)
            FilterAndSortViewHolder.LAYOUT_RES -> FilterAndSortViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}