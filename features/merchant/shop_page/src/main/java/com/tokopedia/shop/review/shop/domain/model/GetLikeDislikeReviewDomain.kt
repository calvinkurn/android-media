package com.tokopedia.shop.review.shop.domain.model

/**
 * @author by nisie on 9/29/17.
 */
class GetLikeDislikeReviewDomain(list: List<LikeDislikeListDomain>?) {
    var list: List<LikeDislikeListDomain>? = null

    init {
        this.list = list
    }
}