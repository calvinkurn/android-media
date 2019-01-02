package com.tokopedia.feedcomponent.view.viewmodel.recommendation

/**
 * @author by yfsx on 04/12/18.
 */
data class RecommendationCardViewModel(
        val image1Url: String = "",
        val image2Url: String = "",
        val image3Url: String = "",
        val profileImageUrl: String = "",
        val badgeUrl: String = "",
        val profileName: String = "",
        val description: String = "",
        val btnText: String = "",
        val isFollowing:Boolean = false
)