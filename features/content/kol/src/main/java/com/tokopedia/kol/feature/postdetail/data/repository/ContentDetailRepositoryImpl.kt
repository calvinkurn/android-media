package com.tokopedia.kol.feature.postdetail.data.repository

import android.text.TextUtils
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedBroadcastTrackerUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetRecommendationPostUseCase
import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailUiModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.LikeContentModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 02/08/22.
 */
class ContentDetailRepositoryImpl @Inject constructor(
    private  val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getRecommendationPostUseCase: GetRecommendationPostUseCase,
    private val likeContentUseCase: SubmitLikeContentUseCase,
    private val followShopUseCase: UpdateFollowStatusUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToWishlistUseCase: AddToWishlistV2UseCase,
    private val submitActionContentUseCase: SubmitActionContentUseCase,
    private val submitReportContentUseCase: SubmitReportContentUseCase,
    private val trackVisitChannelUseCase: FeedBroadcastTrackerUseCase,
    private val trackViewerUseCase: FeedXTrackViewerUseCase,
    private val mapper: ContentDetailMapper,
) : ContentDetailRepository {

    override suspend fun getContentDetail(contentId: String): ContentDetailUiModel {
        return withContext(dispatcher.io) {
            getPostDetailUseCase.executeForCDPRevamp(
                cursor = "",
                detailId = contentId
            )
        }
    }

    override suspend fun getContentRecommendation(
        activityId: String,
        cursor: String
    ): ContentDetailUiModel {
        return withContext(dispatcher.io) {
            val response = getRecommendationPostUseCase.execute(
                cursor = cursor,
                activityId = activityId
            )
            mapper.mapContent(
                response.feedXPostRecommendation.posts,
                response.feedXPostRecommendation.nextCursor
            )
        }
    }

    override suspend fun likeContent(
        contentId: String,
        action: ContentLikeAction,
        rowNumber: Int
    ): LikeContentModel {
        return withContext(dispatcher.io) {
            likeContentUseCase.setRequestParams(
                SubmitLikeContentUseCase.createParam(contentId, action.value)
            )
            val response = likeContentUseCase.executeOnBackground()
            if (response.doLikeKolPost.error.isNotEmpty()) {
                throw MessageErrorException(response.doLikeKolPost.error)
            }
            if (response.doLikeKolPost.data.success != SubmitLikeContentUseCase.SUCCESS) {
                throw CustomUiMessageThrowable(R.string.feed_like_error_message)
            }
            mapper.mapLikeContent(rowNumber, action)
        }
    }

    override suspend fun followShop(shopId: String, action: ShopFollowAction) {
        return withContext(dispatcher.io) {
            followShopUseCase.params = UpdateFollowStatusUseCase.createParams(
                shopId,
                action.value
            )
            val response = followShopUseCase.executeOnBackground()
            if (response.followShop?.success == false) {
                throw CustomUiMessageThrowable(
                    if (action.isFollowing) R.string.feed_follow_error_message
                    else R.string.feed_unfollow_error_message
                )
            }
        }
    }

    override suspend fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
    ): Boolean = withContext(dispatcher.io) {
        val params = AddToCartUseCase.getMinimumParams(
            productId,
            shopId,
            productName = productName,
            price = price,
            userId = userSession.userId
        )
        try {
            addToCartUseCase.setParams(params)
            val response = addToCartUseCase.executeOnBackground()
            if (response.isDataError()) throw MessageErrorException(response.getAtcErrorMessage())
            return@withContext !response.isStatusError()
        } catch (e: Throwable) {
            if (e is ResponseErrorException) throw MessageErrorException(e.localizedMessage)
            else throw e
        }
    }

    override suspend fun addToWishlist(productId: String): Result<AddToWishlistV2Response.Data.WishlistAddV2> {
        return withContext(dispatcher.io) {
            addToWishlistUseCase.setParams(productId, userSession.userId)
            addToWishlistUseCase.executeOnBackground()
        }
    }

    override suspend fun deleteContent(contentId: String) = withContext(dispatcher.io) {
        submitActionContentUseCase.setRequestParams(SubmitActionContentUseCase.paramToDeleteContent(contentId))
        val response = submitActionContentUseCase.executeOnBackground()
        if (TextUtils.isEmpty(response.content.error).not()) {
            throw MessageErrorException(response.content.error)
        }
    }

    override suspend fun reportContent(
        contentId: String,
        reasonType: String,
        reasonMessage: String
    ) = withContext(dispatcher.io) {
        submitReportContentUseCase.setRequestParams(
            SubmitReportContentUseCase.createParam(contentId, reasonType, reasonMessage)
        )
        val response = submitReportContentUseCase.executeOnBackground()
        if (response.content.errorMessage.isNotEmpty()) {
            throw MessageErrorException(response.content.errorMessage)
        }
    }

    override suspend fun trackVisitChannel(channelId: String): Boolean {
        return withContext(dispatcher.io) {
            trackVisitChannelUseCase.setRequestParams(
                FeedBroadcastTrackerUseCase.createParams(channelId)
            )
            val response = trackVisitChannelUseCase.executeOnBackground()
            response.reportVisitChannelTracking.success
        }
    }

    override suspend fun trackViewer(contentId: String): Boolean {
        return withContext(dispatcher.io) {
            trackViewerUseCase.setRequestParams(
                FeedXTrackViewerUseCase.createParams(contentId)
            )
            val response = trackViewerUseCase.executeOnBackground()
            response.feedXTrackViewerResponse.success
        }
    }
}