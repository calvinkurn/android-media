package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.SearchRatingProductUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.*

class SellerReviewListTypeFactory(
        private val filterAndSortListener: FilterAndSortViewHolder.FilterAndSortListener
): BaseAdapterTypeFactory(), TypeFactoryViewHolder {

    override fun type(productRatingOverallUiModel: ProductRatingOverallUiModel): Int {
        return ReviewSummaryViewHolder.LAYOUT_RES
    }

    override fun type(productReviewUiModel: ProductReviewUiModel): Int {
        return SellerReviewListViewHolder.LAYOUT_RES
    }

    override fun type(filterAndSortModel: FilterAndSortModel): Int {
        return FilterAndSortViewHolder.LAYOUT_RES
    }

    override fun type(viewModel: LoadingModel): Int {
        return ReviewSellerLoadingViewHolder.LAYOUT_RES
    }

    override fun type(viewModel: SearchRatingProductUiModel): Int {
        return SearchRatingProductViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ReviewSummaryViewHolder.LAYOUT_RES -> ReviewSummaryViewHolder(parent)
            SellerReviewListViewHolder.LAYOUT_RES -> SellerReviewListViewHolder(parent)
            FilterAndSortViewHolder.LAYOUT_RES -> FilterAndSortViewHolder(parent, filterAndSortListener)
            ReviewSellerLoadingViewHolder.LAYOUT_RES -> ReviewSellerLoadingViewHolder(parent)
            SearchRatingProductViewHolder.LAYOUT_RES -> SearchRatingProductViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}