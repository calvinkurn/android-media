package com.tokopedia.review_shop.shop.data.factory

import com.tokopedia.review_shop.shop.data.mapper.DeleteReviewResponseMapper
import com.tokopedia.review_shop.shop.data.mapper.GetLikeDislikeMapper
import com.tokopedia.review_shop.shop.data.mapper.LikeDislikeMapper
import com.tokopedia.review_shop.shop.data.network.ReputationService
import com.tokopedia.review_shop.shop.data.network.ReviewProductService
import com.tokopedia.review_shop.shop.data.source.CloudDeleteReviewResponseDataSource
import com.tokopedia.review_shop.shop.data.source.CloudGetLikeDislikeDataSource
import com.tokopedia.review_shop.shop.data.source.CloudLikeDislikeDataSource
import com.tokopedia.review_shop.shop.data.source.ReviewShopGetListReviewCloud
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 8/14/17.
 */
class ReputationFactory(private val reputationService: ReputationService?,
                        private val deleteReviewResponseMapper: DeleteReviewResponseMapper?,
                        private val getLikeDislikeMapper: GetLikeDislikeMapper?,
                        private val likeDislikeMapper: LikeDislikeMapper?,
                        private val reputationReviewApi: ReviewProductService?,
                        private val userSession: UserSessionInterface?) {
    fun createCloudDeleteReviewResponseDataSource(): CloudDeleteReviewResponseDataSource {
        return CloudDeleteReviewResponseDataSource(reputationService!!,
                deleteReviewResponseMapper!!, userSession!!)
    }

    fun createCloudGetLikeDislikeDataSource(): CloudGetLikeDislikeDataSource {
        return CloudGetLikeDislikeDataSource(
                reputationService!!,
                getLikeDislikeMapper!!
        )
    }

    fun createCloudLikeDislikeDataSource(): CloudLikeDislikeDataSource {
        return CloudLikeDislikeDataSource(
                reputationService!!,
                likeDislikeMapper!!,
                userSession!!
        )
    }

    fun createCloudGetReviewShopList(): ReviewShopGetListReviewCloud {
        return ReviewShopGetListReviewCloud(
                reputationReviewApi!!
        )
    }

}