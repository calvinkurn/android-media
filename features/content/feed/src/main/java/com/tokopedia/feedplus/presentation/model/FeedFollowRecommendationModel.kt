package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
data class FeedFollowRecommendationModel(
    val title: String,
    val description: String,
    val data: List<Profile>,
    val hasNext: Boolean,
    val cursor: String,
) {

    data class Profile(
        val id: String,
        val name: String,
        val badge: String,
        val type: String,
        val imageUrl: String,
        val videoUrl: String,
        val isFollow: Boolean,
    )
}
