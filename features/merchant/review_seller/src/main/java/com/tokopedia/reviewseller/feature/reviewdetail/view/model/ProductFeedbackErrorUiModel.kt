package com.tokopedia.reviewseller.feature.reviewdetail.view.model

import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

/**
 * Created by Yehezkiel on 28/04/20
 */
data class ProductFeedbackErrorUiModel(
        //False is backend error, and true if list is empty
        val error: Boolean = false
) : BaseSellerReviewDetail {
    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory?): Int {
        TODO("Not yet implemented")
    }
}