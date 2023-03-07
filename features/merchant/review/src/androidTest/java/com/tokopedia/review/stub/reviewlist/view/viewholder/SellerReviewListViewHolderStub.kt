package com.tokopedia.review.stub.reviewlist.view.viewholder

import android.view.View
import com.tokopedia.review.feature.reviewlist.view.viewholder.SellerReviewListViewHolder

class SellerReviewListViewHolderStub(
    view: View,
    sellerReviewListener: SellerReviewListListener
) : SellerReviewListViewHolder(view, sellerReviewListener) {
    override fun bindProductImage(imageUrl: String) {
//        binding.ivItemProduct.loadImage(imageUrl)
    }
}
