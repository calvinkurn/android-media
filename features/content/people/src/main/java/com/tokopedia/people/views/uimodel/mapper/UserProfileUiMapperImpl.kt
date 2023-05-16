package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.feedcomponent.domain.model.UserFeedPostsModel
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.model.*
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.model.VideoPostReimderModel
import com.tokopedia.people.utils.UserProfileVideoMapper
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.content.*
import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
class UserProfileUiMapperImpl @Inject constructor(
    private val userSession: UserSessionInterface,
) : UserProfileUiMapper {

    override fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel {
        return ProfileUiModel(
            userID = response.profileHeader.profile.userID,
            encryptedUserID = response.profileHeader.profile.encryptedUserID,
            imageCover = response.profileHeader.profile.imageCover,
            name = response.profileHeader.profile.name,
            username = response.profileHeader.profile.username,
            biography = response.profileHeader.profile.biography.replace("\n", "<br />"),
            badges = response.profileHeader.profile.badges,
            stats = ProfileStatsUiModel(
                totalPostFmt = response.profileHeader.stats.totalPostFmt,
                totalFollowerFmt = response.profileHeader.stats.totalFollowerFmt,
                totalFollowingFmt = response.profileHeader.stats.totalFollowingFmt,
                totalReviewFmt = getReviewStats(response.profileHeader.stats.extraStats),
            ),
            shareLink = LinkUiModel(
                webLink = response.profileHeader.profile.sharelink.weblink,
                appLink = response.profileHeader.profile.sharelink.applink,
            ),
            liveInfo = LivePlayChannelUiModel(
                isLive = response.profileHeader.profile.liveplaychannel.islive,
                channelId = response.profileHeader.profile.liveplaychannel.liveplaychannelid,
                channelLink = LinkUiModel(
                    webLink = response.profileHeader.profile.liveplaychannel.liveplaychannellink.weblink,
                    appLink = response.profileHeader.profile.liveplaychannel.liveplaychannellink.applink,
                ),
            ),
            isBlocking = response.profileHeader.isBlocking,
            isBlockedBy = response.profileHeader.isBlockedBy,
        )
    }

    override fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel {
        return FollowInfoUiModel(
            userID = response.profileHeader.items.firstOrNull()?.userID ?: "",
            encryptedUserID = response.profileHeader.items.firstOrNull()?.encryptedUserID ?: "",
            status = response.profileHeader.items.firstOrNull()?.status ?: false,
        )
    }

    override fun mapUserWhitelist(response: GetCheckWhitelistResponse): ProfileWhitelistUiModel {
        val authorUgc = response.whitelist.authors.find { it.type == ContentCommonUserType.TYPE_USER }

        return ProfileWhitelistUiModel(
            isWhitelist = authorUgc != null,
            /** hasUsername has the same value across all content creation type (post, livestream, shortvideo) */
            hasUsername = authorUgc?.post?.hasUsername ?: false,
            hasAcceptTnc = authorUgc?.hasAcceptTnc ?: false,
        )
    }

    override fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel {
        return with(response.playToggleChannelReminder) {
            if (header.status == SUCCESS_UPDATE_REMINDER_CODE) {
                MutationUiModel.Success(header.message)
            } else {
                MutationUiModel.Error(header.message)
            }
        }
    }

    override fun mapProfileTab(response: UserProfileTabModel): ProfileTabUiModel {
        return with(response.feedXProfileTabs) {
            val expectedTabs = tabs.filter {
                it.isActive && (it.key == TAB_KEY_FEEDS || it.key == TAB_KEY_VIDEO || it.key == TAB_KEY_REVIEW)
            }
            ProfileTabUiModel(
                showTabs = expectedTabs.size > 1,
                tabs = if (expectedTabs.isNotEmpty()) {
                    expectedTabs.map {
                        ProfileTabUiModel.Tab(
                            title = it.title,
                            key = it.key,
                            position = it.position,
                        )
                    }.sortedBy { it.position }
                } else {
                    emptyList()
                },
            )
        }
    }

    override fun mapFeedPosts(response: UserFeedPostsModel): UserFeedPostsUiModel {
        return with(response.feedXProfileGetProfilePosts) {
            UserFeedPostsUiModel(
                pagination = PaginationUiModel(
                    cursor = pagination.cursor,
                    hasNext = pagination.hasNext,
                    totalData = pagination.totalData,
                ),
                posts = posts.map { post ->
                    PostUiModel(
                        id = post.id,
                        appLink = post.appLink,
                        media = post.media.map { media ->
                            MediaUiModel(
                                appLink = media.appLink,
                                coverURL = media.coverURL,
                                id = media.id,
                                mediaURL = media.mediaURL,
                                type = media.type,
                                webLink = media.webLink,
                            )
                        }
                    )
                }
            )
        }
    }

    override fun mapPlayVideo(response: UserPostModel): UserPlayVideoUiModel {
        val videoList = response.playGetContentSlot.data.firstOrNull()?.items?.map {
            UserProfileVideoMapper.map(it, userSession.userId)
        } ?: emptyList()
        val nextCursor = response.playGetContentSlot.playGetContentSlot.nextCursor

        return UserPlayVideoUiModel(
            items = videoList,
            nextCursor = nextCursor,
            status = UserPlayVideoUiModel.Status.Success,
        )
    }

    override fun mapProfileSettings(response: GetProfileSettingsResponse): List<ProfileSettingsUiModel> {
        return response.data.settingsProfile.map {
            ProfileSettingsUiModel(
                settingID = it.settingID,
                title = it.title,
                isEnabled = it.enabled,
            )
        }
    }

    override fun mapUserReviewList(
        response: GetUserReviewListResponse,
        currentPage: Int,
    ): UserReviewUiModel {
        return UserReviewUiModel(
            reviewList = response.data.reviewList.map {
                UserReviewUiModel.Review(
                    feedbackID = it.feedbackID,
                    product = UserReviewUiModel.Product(
                        productID = it.product.productID,
                        productName = it.product.productName,
                        productImageURL = it.product.productImageURL,
                        productPageURL = it.product.productPageURL,
                        productStatus = it.product.productStatus,
                        productVariant = UserReviewUiModel.ProductVariant(
                            variantID = it.product.productVariant.variantID,
                            variantName = it.product.productVariant.variantName,
                        )
                    ),
                    rating = it.rating,
                    reviewText = it.reviewText,
                    reviewTime = it.reviewTime,
                    attachments = it.attachments.map { attachment ->
                        UserReviewUiModel.Attachment(
                            attachmentID = attachment.attachmentID,
                            thumbnailURL = attachment.thumbnailURL,
                            fullsizeURL = attachment.fullsizeURL,
                        )
                    },
                    videoAttachments = it.videoAttachments.map { videoAttachment ->
                        UserReviewUiModel.VideoAttachment(
                            attachmentID = videoAttachment.attachmentID,
                            videoUrl = videoAttachment.videoUrl,
                        )
                    },
                    likeDislike = UserReviewUiModel.LikeDislike(
                        totalLike = it.likeDislike.totalLike,
                        likeStatus = it.likeDislike.likeStatus,
                    )
                )
            },
            page = currentPage + 1,
            hasNext = response.data.hasNext,
            status = UserReviewUiModel.Status.Success,
        )
    }

    override fun mapSetLikeStatus(response: SetLikeStatusResponse): UserReviewUiModel.LikeDislike {
        return UserReviewUiModel.LikeDislike(
            totalLike = response.data.totalLike,
            likeStatus = response.data.likeStatus,
        )
    }

    private fun getReviewStats(extraStats: List<ExtraStats>): String {
        return extraStats.firstOrNull { it.field == EXTRA_STATS_REVIEWS }?.countFmt.orEmpty()
    }

    companion object {
        private const val TAB_KEY_FEEDS = "feeds"
        private const val TAB_KEY_VIDEO = "video"
        private const val TAB_KEY_REVIEW = "review"
        private const val SUCCESS_UPDATE_REMINDER_CODE = 200
        private const val SUCCESS_UNFOLLOW_CODE = "1"
        private const val EXTRA_STATS_REVIEWS = "reviews"
    }
}
