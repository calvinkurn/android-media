package com.tokopedia.people.views.uimodel.profile

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class ProfileStatsUiModel(
    val totalPostFmt: String,
    val totalFollowerFmt: String,
    val totalFollowingFmt: String,
) {
    companion object {
        val Empty: ProfileStatsUiModel
            get() = ProfileStatsUiModel(
                totalPostFmt = "",
                totalFollowerFmt = "",
                totalFollowingFmt = "",
            )
    }
}
