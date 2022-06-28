package com.tokopedia.people.views.uimodel.profile


/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class ProfileStatsUiModel(
    val totalPost: Long,
    val totalPostFmt: String,
    val totalFollower: Long,
    val totalFollowerFmt: String,
    val totalFollowing: Long,
    val totalFollowingFmt: String
) {
    companion object {
        val Empty: ProfileStatsUiModel
            get() = ProfileStatsUiModel(
                totalPost = 0L,
                totalPostFmt = "",
                totalFollower = 0L,
                totalFollowerFmt = "",
                totalFollowing = 0L,
                totalFollowingFmt = "",
            )
    }
}