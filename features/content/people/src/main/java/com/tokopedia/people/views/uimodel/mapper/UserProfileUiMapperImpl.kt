package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.content.common.model.AuthorItem
import com.tokopedia.content.common.model.Authors
import com.tokopedia.content.common.model.Creation
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.util.remoteconfig.PlayShortsEntryPointRemoteConfig
import com.tokopedia.feedcomponent.domain.model.UserFeedPostsModel
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.people.model.ExtraStats
import com.tokopedia.people.model.GetProfileSettingsResponse
import com.tokopedia.people.model.GetUserReviewListResponse
import com.tokopedia.people.model.Profile
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.SetLikeStatusResponse
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.model.UserProfileTabModel
import com.tokopedia.people.model.VideoPostReimderModel
import com.tokopedia.people.utils.UserProfileVideoMapper
import com.tokopedia.people.utils.remoteconfig.UserProfileRemoteConfig
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.content.MediaUiModel
import com.tokopedia.people.views.uimodel.content.PaginationUiModel
import com.tokopedia.people.views.uimodel.content.PostUiModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.LinkUiModel
import com.tokopedia.people.views.uimodel.profile.LivePlayChannelUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileCreationInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileStatsUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
class UserProfileUiMapperImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val playShortsEntryPointRemoteConfig: PlayShortsEntryPointRemoteConfig,
    private val userProfileRemoteConfig: UserProfileRemoteConfig
) : UserProfileUiMapper {

    override fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel {
        return ProfileUiModel(
            userID = response.profileHeader.profile.userID,
            encryptedUserID = response.profileHeader.profile.encryptedUserID,
            imageCover = response.profileHeader.profile.imageCover,
            name = response.profileHeader.profile.name,
            username = response.profileHeader.profile.username,
            biography = response.profileHeader.profile.biography.replace("\n", "<br />"),
            stats = ProfileStatsUiModel(
                totalPostFmt = response.profileHeader.stats.totalPostFmt,
                totalFollowerFmt = response.profileHeader.stats.totalFollowerFmt,
                totalFollowingFmt = response.profileHeader.stats.totalFollowingFmt,
                totalReviewFmt = getReviewStats(response.profileHeader.stats.extraStats)
            ),
            shareLink = LinkUiModel(
                webLink = response.profileHeader.profile.sharelink.weblink,
                appLink = response.profileHeader.profile.sharelink.applink
            ),
            liveInfo = LivePlayChannelUiModel(
                isLive = response.profileHeader.profile.liveplaychannel.islive,
                channelId = response.profileHeader.profile.liveplaychannel.liveplaychannelid,
                channelLink = LinkUiModel(
                    webLink = response.profileHeader.profile.liveplaychannel.liveplaychannellink.weblink,
                    appLink = response.profileHeader.profile.liveplaychannel.liveplaychannellink.applink
                )
            ),
            isBlocking = response.profileHeader.isBlocking,
            isBlockedBy = response.profileHeader.isBlockedBy,
            badges = response.profileHeader.profile.profileBadges.map(::mapBadge)
        )
    }

    private fun mapBadge(badge: Profile.Badge): ProfileUiModel.Badge {
        return ProfileUiModel.Badge(
            badge.badgeUrl,
            badge.isClickable,
            if (badge.isClickable) {
                ProfileUiModel.Badge.Detail(
                    badge.bottomSheetTitle,
                    badge.bottomSheetDesc
                )
            } else {
                null
            }
        )
    }

    override fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel {
        return FollowInfoUiModel(
            userID = response.profileHeader.items.firstOrNull()?.userID ?: "",
            encryptedUserID = response.profileHeader.items.firstOrNull()?.encryptedUserID ?: "",
            status = response.profileHeader.items.firstOrNull()?.status ?: false
        )
    }

    override fun mapCreationInfo(response: Creation): ProfileCreationInfoUiModel {
        fun getAuthorTypeUser(authors: List<Authors>): Authors? {
            return authors.find { it.type == ContentCommonUserType.TYPE_USER }
        }

        fun getButtonTypePost(items: List<AuthorItem>): Boolean {
            return items.find { it.type == TYPE_POST }?.isActive.orFalse()
        }

        fun getButtonTypeLiveStream(items: List<AuthorItem>): Boolean {
            return items.find { it.type == TYPE_LIVE_STREAM }?.isActive.orFalse()
        }

        fun getButtonTypeShortVideo(items: List<AuthorItem>): Boolean {
            return items.find { it.type == TYPE_SHORT_VIDEO }?.isActive.orFalse() &&
                playShortsEntryPointRemoteConfig.isShowEntryPoint()
        }

        val getAuthorItemTypeUser = getAuthorTypeUser(response.authors)?.items
            ?: return ProfileCreationInfoUiModel()

        return ProfileCreationInfoUiModel(
            isActive = response.isActive,
            showPost = getButtonTypePost(getAuthorItemTypeUser),
            showLiveStream = getButtonTypeLiveStream(getAuthorItemTypeUser),
            showShortVideo = getButtonTypeShortVideo(getAuthorItemTypeUser)
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
                if (ProfileTabUiModel.mapToKey(it.key) == ProfileTabUiModel.Key.Review) {
                    it.isActive && userProfileRemoteConfig.isEnableReviewTab()
                } else {
                    it.isActive && ProfileTabUiModel.mapToKey(it.key) != ProfileTabUiModel.Key.Unknown
                }
            }
            ProfileTabUiModel(
                showTabs = expectedTabs.size > 1,
                tabs = if (expectedTabs.isNotEmpty()) {
                    expectedTabs.map {
                        ProfileTabUiModel.Tab(
                            title = it.title,
                            key = ProfileTabUiModel.mapToKey(it.key),
                            position = it.position,
                            isNew = false
                        )
                    }.sortedBy { it.position }
                } else {
                    emptyList()
                }
            )
        }
    }

    override fun mapFeedPosts(response: UserFeedPostsModel): UserFeedPostsUiModel {
        return with(response.feedXProfileGetProfilePosts) {
            UserFeedPostsUiModel(
                pagination = PaginationUiModel(
                    cursor = pagination.cursor,
                    hasNext = pagination.hasNext,
                    totalData = pagination.totalData
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
                                webLink = media.webLink
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
            status = UserPlayVideoUiModel.Status.Success
        )
    }

    override fun mapProfileSettings(response: GetProfileSettingsResponse): List<ProfileSettingsUiModel> {
        return response.data.settingsProfile.map {
            ProfileSettingsUiModel(
                settingID = it.settingID,
                title = it.title,
                isEnabled = it.enabled
            )
        }
    }

    override fun mapUserReviewList(
        response: GetUserReviewListResponse,
        currentPage: Int
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
                            variantName = it.product.productVariant.variantName
                        )
                    ),
                    rating = it.rating,
                    reviewText = it.reviewText,
                    reviewTime = it.reviewTime,
                    attachments = it.videoAttachments.map { attachment ->
                        UserReviewUiModel.Attachment.Video(
                            attachmentID = attachment.attachmentID,
                            mediaUrl = attachment.videoUrl
                        )
                    } + it.attachments.map { attachment ->
                        UserReviewUiModel.Attachment.Image(
                            attachmentID = attachment.attachmentID,
                            thumbnailUrl = attachment.thumbnailURL,
                            fullSizeUrl = attachment.fullsizeURL
                        )
                    }.take(MAX_REVIEW_MEDIA),
                    likeDislike = UserReviewUiModel.LikeDislike(
                        totalLike = it.likeDislike.totalLike,
                        isLike = UserProfileLikeStatusMapper.isLike(it.likeDislike.likeStatus)
                    ),
                    isReviewTextExpanded = false
                )
            },
            page = currentPage + 1,
            hasNext = response.data.hasNext,
            status = UserReviewUiModel.Status.Success
        )
    }

    override fun mapSetLikeStatus(response: SetLikeStatusResponse): UserReviewUiModel.LikeDislike {
        return UserReviewUiModel.LikeDislike(
            totalLike = response.data.totalLike,
            isLike = UserProfileLikeStatusMapper.isLike(response.data.likeStatus)
        )
    }

    private fun getReviewStats(extraStats: List<ExtraStats>): String {
        return extraStats.firstOrNull { it.field == EXTRA_STATS_REVIEWS }?.countFmt.orEmpty()
    }

    companion object {
        private const val TYPE_POST = "post"
        private const val TYPE_LIVE_STREAM = "livestream"
        private const val TYPE_SHORT_VIDEO = "shortvideo"
        private const val TAB_KEY_FEEDS = "feeds"
        private const val TAB_KEY_VIDEO = "video"
        private const val TAB_KEY_REVIEW = "review"
        private const val SUCCESS_UPDATE_REMINDER_CODE = 200
        private const val EXTRA_STATS_REVIEWS = "reviews"
        private const val MAX_REVIEW_MEDIA = 5
    }
}
