package com.tokopedia.review.feature.credibility.presentation.uimodel

data class ReviewCredibilityHeaderUiModel(
    val reviewerProfilePicture: String,
    val reviewerName: String,
    val reviewerJoinDate: String,
    val reviewerProfileButtonText: String,
    val reviewerProfileButtonUrl: String,
    val trackingData: TrackingData
) {
    data class TrackingData(
        val reviewerUserId: String,
        val viewerUserId: String,
        val productId: String,
        val pageSource: String
    )
}
