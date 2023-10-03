package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel

/**
 * Created By : Jonathan Darwin on July 05, 2023
 */
interface FeedFollowRecommendationListener {

    fun onImpressProfile(
        profile: FeedFollowRecommendationModel.Profile
    )

    fun onClickFollow(
        profile: FeedFollowRecommendationModel.Profile
    )
    
    fun onCloseProfileRecommendation(
        profile: FeedFollowRecommendationModel.Profile
    )

    fun onClickProfileRecommendation(
        profile: FeedFollowRecommendationModel.Profile
    )

    fun onSwipeProfileRecommendation()

    fun reloadProfileRecommendation()

    fun onLoadNextProfileRecommendation()

    fun onClickViewOtherContent()

    fun onErrorPlayingVideo()
}
