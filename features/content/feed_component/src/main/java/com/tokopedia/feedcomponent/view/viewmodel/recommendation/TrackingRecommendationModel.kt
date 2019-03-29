package com.tokopedia.feedcomponent.view.viewmodel.recommendation

/**
 * @author by milhamj on 10/01/19.
 */
data class TrackingRecommendationModel (
        val templateType: String = "",
        val activityName: String = "",
        val trackingType: String = "",
        val mediaType: String = "",
        val authorName: String = "",
        val authorType: String = "",
        val authorId: Int = 0,
        val cardPosition: Int = 0
)