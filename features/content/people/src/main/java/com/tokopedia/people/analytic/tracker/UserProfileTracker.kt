package com.tokopedia.people.analytic.tracker

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem

interface UserProfileTracker {

    fun openUserProfile(userId: String, live: Boolean)

    fun clickBack(userId: String, self: Boolean)

    fun clickShare(userId: String, self: Boolean)

    fun clickBurgerMenu(userId: String, self: Boolean)

    fun clickProfilePicture(userId: String, self: Boolean, activityId: String)

    fun clickFollowers(userId: String, self: Boolean)

    fun clickFollowing(userId: String, self: Boolean)

    fun clickSelengkapnya(userId: String, self: Boolean)

    fun clickFollow(userId: String, self: Boolean)

    fun clickUnfollow(userId: String, self: Boolean)

    fun clickVideoTab(userId: String, self: Boolean)

    fun impressionVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
        imageUrl: String,
        videoPosition: Int,
    )

    fun clickVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
        imageUrl: String,
        videoPosition: Int,
    )

    fun clickFeedTab(userId: String, self: Boolean)

    fun impressionPost(
        userId: String,
        self: Boolean,
        activityId: String,
        imageUrl: String,
        postPosition: Int,
        mediaType: String,
    )

    fun clickPost(
        userId: String,
        self: Boolean,
        activityId: String,
        imageUrl: String,
        postPosition: Int,
        mediaType: String,
    )

    fun openFollowersTab(userId: String)

    fun clickUserFollowers(userId: String, self: Boolean)

    fun clickFollowFromFollowers(userId: String, self: Boolean)

    fun clickUnfollowFromFollowers(userId: String, self: Boolean)

    fun openFollowingTab(userId: String)

    fun clickUserFollowing(userId: String, self: Boolean)

    fun clickFollowFromFollowing(userId: String, self: Boolean)

    fun clickUnfollowFromFollowing(userId: String, self: Boolean)

    fun impressionProfileCompletionPrompt(userId: String)

    fun clickProfileCompletionPrompt(userId: String)

    fun impressionProfileRecommendation(
        userId: String,
        shops: ShopRecomUiModelItem,
        postPosition: Int,
    )

    fun clickProfileRecommendation(
        userId: String,
        shopId: String,
        imageUrl: String,
        postPosition: Int,
    )

    fun clickFollowProfileRecommendation(userId: String, shopId: String)

    fun clickCreatePost(userId: String)

    fun impressionOnBoardingBottomSheetWithUsername(userId: String)

    fun clickLanjutOnBoardingBottomSheetWithUsername(userId: String)

    fun impressionOnBoardingBottomSheetWithoutUsername(userId: String)

    fun clickLanjutOnBoardingBottomSheetWithoutUsername(userId: String)

    fun clickEditProfileButtonInOwnProfile(userId: String)

    fun clickShareButton(userId: String, self: Boolean)

    fun clickCloseShareButton(userId: String, self: Boolean)

    fun clickShareChannel(userId: String, self: Boolean, channel: String)

    fun viewShareChannel(userId: String, self: Boolean)

    fun viewScreenshotShareBottomsheet(userId: String, self: Boolean)

    fun clickCloseScreenshotShareBottomsheet(userId: String, self: Boolean)

    fun clickChannelScreenshotShareBottomsheet(userId: String, self: Boolean)

    fun clickAccessMedia(userId: String, self: Boolean, allow: String)

    fun clickCreateShorts(userId: String)

    fun viewCreateShorts(userId: String)

    fun sendAll()
}
