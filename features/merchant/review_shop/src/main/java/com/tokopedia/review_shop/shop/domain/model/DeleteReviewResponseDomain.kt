package com.tokopedia.review_shop.shop.domain.model

/**
 * @author by nisie on 9/27/17.
 */
class DeleteReviewResponseDomain(isSuccess: Int) {
    val isSuccess: Boolean

    init {
        this.isSuccess = isSuccess == 1
    }
}