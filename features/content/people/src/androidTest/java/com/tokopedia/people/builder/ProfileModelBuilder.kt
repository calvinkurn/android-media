package com.tokopedia.people.builder

import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.LinkUiModel
import com.tokopedia.people.views.uimodel.profile.LivePlayChannelUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileCreationInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileStatsUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
class ProfileModelBuilder {

    fun buildProfile(
        userId: String = "123"
    ): ProfileUiModel {
        return ProfileUiModel(
            userID = userId,
            encryptedUserID = userId,
            imageCover = "",
            name = "Jonathan",
            username = "jonathan",
            biography = "",
            badges = listOf(),
            stats = ProfileStatsUiModel.Empty,
            shareLink = LinkUiModel.Empty,
            liveInfo = LivePlayChannelUiModel.Empty,
            isBlocking = false,
            isBlockedBy = false,
        )
    }

    fun buildFollowInfo(
        userId: String = "123",
        isFollowed: Boolean = false,
    ): FollowInfoUiModel {
        return FollowInfoUiModel(
            userID = userId,
            encryptedUserID = userId,
            status = isFollowed,
        )
    }

    fun buildProfileCreationInfo(
        isActive: Boolean = false,
        showPost: Boolean = false,
        showLiveStream: Boolean = false,
        showShortVideo: Boolean = false,
    ): ProfileCreationInfoUiModel {
        return ProfileCreationInfoUiModel(
            isActive = isActive,
            showPost = showPost,
            showLiveStream = showLiveStream,
            showShortVideo = showShortVideo,
        )
    }

    fun buildProfileTab(
        showTabs: Boolean = true,
        isShowFeedTab: Boolean = true,
        isShowVideoTab: Boolean = true,
        isShowReviewTab: Boolean = true,
    ): ProfileTabUiModel {
        return ProfileTabUiModel(
            showTabs = showTabs,
            tabs = mutableListOf<ProfileTabUiModel.Tab>().apply {
                var position = 1

                if (isShowFeedTab) {
                    add(ProfileTabUiModel.Tab(title = "Feeds", key = "feeds", position = position++))
                }
                if (isShowVideoTab) {
                    add(ProfileTabUiModel.Tab(title = "Video", key = "video", position = position++))
                }
                if (isShowReviewTab) {
                    add(ProfileTabUiModel.Tab(title = "Review", key = "review", position = position++))
                }
            }
        )
    }

    fun buildProfileSettings(
        isShowReview: Boolean = true,
    ): List<ProfileSettingsUiModel> {
        return listOf(
            ProfileSettingsUiModel(
                settingID = ProfileSettingsUiModel.SETTING_ID_REVIEW,
                title = "Show / hide review",
                isEnabled = isShowReview
            )
        )
    }

    fun buildReviewList(
        size: Int = 5,
        page: Int = 1,
        hasNext: Boolean = true,
        status: UserReviewUiModel.Status = UserReviewUiModel.Status.Success,
    ): UserReviewUiModel {
        return UserReviewUiModel(
            reviewList = List(size) {
                UserReviewUiModel.Review(
                    feedbackID = it.toString(),
                    product = UserReviewUiModel.Product(
                        productID = it.toString(),
                        productName = "Product $it",
                        productImageURL = "",
                        productPageURL = "",
                        productStatus = 0,
                        productVariant = UserReviewUiModel.ProductVariant(
                            variantID = it.toString(),
                            variantName = "Variant $it"
                        )
                    ),
                    rating = it.coerceAtMost(5),
                    reviewText = "review $it",
                    reviewTime = "1 bulan lalu",
                    attachments = listOf(
                        UserReviewUiModel.Attachment.Image(
                            attachmentID = it.toString(),
                            thumbnailUrl = "",
                            fullSizeUrl = "",
                        ),
                        UserReviewUiModel.Attachment.Video(
                            attachmentID = (it + 1).toString(),
                            mediaUrl = "",
                        )
                    ),
                    likeDislike = UserReviewUiModel.LikeDislike(
                        totalLike = 123,
                        likeStatus = 0,
                    ),
                    isReviewTextExpanded = false,
                )
            },
            page = page,
            hasNext = hasNext,
            status = status,
        )
    }
}
