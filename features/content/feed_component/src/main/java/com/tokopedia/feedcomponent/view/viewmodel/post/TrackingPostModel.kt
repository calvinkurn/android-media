package com.tokopedia.feedcomponent.view.viewmodel.post

/**
 * @author by milhamj on 10/01/19.
 */
data class TrackingPostModel (
        val templateType: String = "",
        val activityName: String = "",
        val trackingType: String = "",
        val mediaType: String = "",
        val tagsType: String = "",
        val redirectUrl: String = "",
        val postId: Int = 0,
        val totalContent: Int = 0
)