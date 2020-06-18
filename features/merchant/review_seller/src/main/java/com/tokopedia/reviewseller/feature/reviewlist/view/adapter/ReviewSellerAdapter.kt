package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel

class ReviewSellerAdapter(
        sellerReviewListTypeFactory: SellerReviewListTypeFactory
): BaseListAdapter<Visitable<*>,SellerReviewListTypeFactory>(sellerReviewListTypeFactory), DataEndlessScrollListener.OnDataEndlessScrollListener {

    private var productReviewListViewModel: MutableList<ProductReviewUiModel> = mutableListOf()

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

    override val endlessDataSize: Int
        get() = productReviewListViewModel.size

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            val itemPosition = visitables.indexOf(loadingModel)
            visitables.remove(loadingModel)
            notifyItemRemoved(itemPosition)
        } else if (visitables.contains(loadingMoreModel)) {
            val itemPosition = visitables.indexOf(loadingMoreModel)
            visitables.remove(loadingMoreModel)
            notifyItemRemoved(itemPosition)
        }
    }
}