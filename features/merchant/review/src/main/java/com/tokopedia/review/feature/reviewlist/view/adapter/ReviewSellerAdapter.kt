package com.tokopedia.review.feature.reviewlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel

class ReviewSellerAdapter(
        sellerReviewListTypeFactory: SellerReviewListTypeFactory
): BaseListAdapter<Visitable<*>,SellerReviewListTypeFactory>(sellerReviewListTypeFactory) {

    companion object{
        const val PAYLOAD_SUMMARY_PERIOD = 512
    }

    fun setProductListReviewData(productListReviewUiModel: List<ProductReviewUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(productListReviewUiModel)
        notifyItemRangeInserted(lastIndex, productListReviewUiModel.size)
    }

    fun setProductRatingOverallData(data: ProductRatingOverallUiModel) {
        val lastIndex = visitables.size
        visitables.add(data)
        notifyItemInserted(lastIndex)
    }

    fun updateDatePeriod(datePeriod: String) {
        val reviewSummaryData = visitables.find { it is ProductRatingOverallUiModel }
        val indexOfReviewSummary = visitables.indexOf(reviewSummaryData)

        if (indexOfReviewSummary != -1) {
            (reviewSummaryData as? ProductRatingOverallUiModel)?.period = datePeriod
            notifyItemChanged(indexOfReviewSummary, PAYLOAD_SUMMARY_PERIOD)
        }
    }
}