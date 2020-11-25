package com.tokopedia.review.feature.reviewlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.common.util.DataEndlessScrollListener
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel

class ReviewSellerAdapter(
        sellerReviewListTypeFactory: SellerReviewListTypeFactory
): BaseListAdapter<Visitable<*>,SellerReviewListTypeFactory>(sellerReviewListTypeFactory), DataEndlessScrollListener.OnDataEndlessScrollListener {

    companion object{
        const val PAYLOAD_SUMMARY_PERIOD = 512
    }

    var productReviewListViewModel: MutableList<ProductReviewUiModel> = mutableListOf()

    fun setProductListReviewData(productListReviewUiModel: List<ProductReviewUiModel>) {
        val lastIndex = visitables.size
        productReviewListViewModel.addAll(productListReviewUiModel)
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

    override val endlessDataSize: Int
        get() = productReviewListViewModel.size

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            val itemPosition = visitables.indexOf(loadingModel)
            visitables.remove(loadingModel)
            if(itemPosition != -1) {
                notifyItemRemoved(itemPosition)
            }
        } else if (visitables.contains(loadingMoreModel)) {
            val itemPosition = visitables.indexOf(loadingMoreModel)
            visitables.remove(loadingMoreModel)
            if(itemPosition != -1) {
                notifyItemRemoved(itemPosition)
            }
        }
    }
}