package com.tokopedia.kol.feature.postdetail.domain

import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendationData
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampDataUiModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response

/**
 * Created by meyta.taliti on 02/08/22.
 */
interface ContentDetailRepository {

    suspend fun getContentDetail(
        contentId: String
    ): ContentDetailRevampDataUiModel

    suspend fun getContentRecommendation(
        activityId: String,
        cursor: String,
    ): FeedXPostRecommendationData

    suspend fun likeContent(
        contentId: String,
        action: ContentLikeAction,
    )

    suspend fun followShop(
        shopId: String,
        action: ShopFollowAction,
    )

    suspend fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
    ): Boolean

    suspend fun addToWishlist(
        productId: String
    ): Result<AddToWishlistV2Response.Data.WishlistAddV2>

    suspend fun deleteContent(
        contentId: String
    )

    suspend fun reportContent(
        contentId: String,
        reasonType: String,
        reasonMessage: String,
    )

    suspend fun trackVisitChannel(
        channelId: String,
    ): Boolean

    suspend fun trackViewer(
        contentId: String
    ): Boolean
}