package com.tokopedia.kol.feature.postdetail.data.repository

import android.text.TextUtils
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.domain.usecase.CheckUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedBroadcastTrackerUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetUserProfileFeedPostsUseCase
import com.tokopedia.feedcomponent.domain.usecase.PostUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapper
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetRecommendationPostUseCase
import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.user.session.UserSessionInterface
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
    private val checkUpcomingCampaignReminderUseCase: CheckUpcomingCampaignReminderUseCase,
    private val postUpcomingCampaignReminderUseCase: PostUpcomingCampaignReminderUseCase,
    private val getUserProfileFeedPostUseCase: GetUserProfileFeedPostsUseCase,
    private val followUserUseCase: ProfileFollowUseCase,
    private val unfollowUserUseCase: ProfileUnfollowedUseCase,
    private val mapper: ContentDetailMapper,
    private val profileMutationMapper: ProfileMutationMapper,
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

    override suspend fun getFeedPosts(
        userID: String,
        cursor: String,
        limit: Int,
    ): ContentDetailUiModel {
        return withContext(dispatcher.io) {
            val response = getUserProfileFeedPostUseCase.executeOnBackground(
                    userID = userID,
                    cursor = cursor,
                    limit = limit,
                )
            return@withContext mapper.mapFeedPosts(response)
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

    override suspend fun followShop(
        shopId: String,
        action: ShopFollowAction,
        rowNumber: Int,
        isFollowedFromRSRestrictionBottomSheet: Boolean
    ): ShopFollowModel {
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
            mapper.mapShopFollow(rowNumber, action, isFollowedFromRSRestrictionBottomSheet)
        }
    }

    override suspend fun followUnfollowUser(isFollow: Boolean, encryptedUserId: String): MutationUiModel {
        return withContext(dispatcher.io) {
            if (isFollow) {
                profileMutationMapper.mapUnfollow(
                    unfollowUserUseCase.executeOnBackground(encryptedUserId)
                )
            } else {
                profileMutationMapper.mapFollow(
                    followUserUseCase.executeOnBackground(encryptedUserId)
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

    override suspend fun addToWishlist(rowNumber: Int, productId: String): WishlistContentModel =
        withContext(dispatcher.io) {
            addToWishlistUseCase.setParams(productId, userSession.userId)
            addToWishlistUseCase.executeOnBackground()
            mapper.mapWishlistData(rowNumber, productId)
        }


    override suspend fun deleteContent(contentId: String, rowNumber: Int): DeleteContentModel = withContext(dispatcher.io) {
        submitActionContentUseCase.setRequestParams(SubmitActionContentUseCase.paramToDeleteContent(contentId))
        val response = submitActionContentUseCase.executeOnBackground()
        if (TextUtils.isEmpty(response.content.error).not()) {
            throw MessageErrorException(response.content.error)
        }
        mapper.mapDeleteContent(rowNumber)
    }

    override suspend fun reportContent(
        contentId: String,
        reasonType: String,
        reasonMessage: String,
        rowNumber: Int
    ): ReportContentModel = withContext(dispatcher.io) {
        submitReportContentUseCase.setRequestParams(
            SubmitReportContentUseCase.createParam(contentId, reasonType, reasonMessage)
        )
        val response = submitReportContentUseCase.executeOnBackground()
        if (response.content.errorMessage.isNotEmpty()) {
            throw MessageErrorException(response.content.errorMessage)
        }
        mapper.mapReportContent(rowNumber)
    }

    override suspend fun trackVisitChannel(channelId: String, rowNumber: Int): VisitContentModel {
        return withContext(dispatcher.io) {
            trackVisitChannelUseCase.setRequestParams(
                FeedBroadcastTrackerUseCase.createParams(channelId)
            )
            trackVisitChannelUseCase.executeOnBackground()
            mapper.mapVisitChannel(rowNumber)
        }
    }

    override suspend fun trackViewer(contentId: String, rowNumber: Int): VisitContentModel {
        return withContext(dispatcher.io) {
            trackViewerUseCase.setRequestParams(
                FeedXTrackViewerUseCase.createParams(contentId)
            )
            trackViewerUseCase.executeOnBackground()
            mapper.mapVisitChannel(rowNumber)
        }
    }

    override suspend fun checkUpcomingCampaign(campaignId: Long): Boolean {
        return withContext(dispatcher.io) {
            val response = checkUpcomingCampaignReminderUseCase.apply {
                setRequestParams(CheckUpcomingCampaignReminderUseCase.createParam(campaignId))
            }.executeOnBackground()
            return@withContext response.response.isAvailable
        }
    }

    override suspend fun subscribeUpcomingCampaign(campaignId: Long, reminderType: FeedASGCUpcomingReminderStatus): Pair<Boolean, String> {
        return withContext(dispatcher.io) {
            val response = postUpcomingCampaignReminderUseCase.apply {
                setRequestParams(PostUpcomingCampaignReminderUseCase.createParam(campaignId, reminderType).parameters)
            }.executeOnBackground()
            return@withContext Pair(response.response.success, if(response.response.errorMessage.isNotEmpty()) response.response.errorMessage else response.response.message) }
    }
}
