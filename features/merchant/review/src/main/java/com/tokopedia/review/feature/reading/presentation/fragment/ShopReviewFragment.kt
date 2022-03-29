package com.tokopedia.review.feature.reading.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.review.common.util.ReviewConstants

/**
 * Created by @ilhamsuaib on 29/03/22.
 */

class ShopReviewFragment : ReadReviewFragment() {

    companion object {

        fun createNewInstance(
            productId: String = "",
            shopId: String = "",
            isProductReview: Boolean
        ): ShopReviewFragment {
            return ShopReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewConstants.ARGS_PRODUCT_ID, productId)
                    putString(ReviewConstants.ARGS_SHOP_ID, shopId)
                    putBoolean(ReviewConstants.ARGS_IS_PRODUCT_REVIEW, isProductReview)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideRatingView()
    }
}