package com.tokopedia.feedcomponent.view.viewmodel.post

/**
 * @author by milhamj on 10/01/19.
 */
data class TrackingPostModel (
        val templateType: String = "",
        val activityName: String = "",
        val trackingType: String = "",
        val mediaType: String = "",
        val mediaUrl: String = "",
        val tagsType: String = "",
        val redirectUrl: String = "",
        val authorId: String = "",
        val postId: String = "",
        val totalContent: Int = 0,
        val recomId: Long = 0
) {
    fun copy(): TrackingPostModel {
        return TrackingPostModel(templateType,
                activityName,
                trackingType,
                mediaType,
                mediaUrl,
                tagsType,
                redirectUrl,
                authorId,
                postId,
                totalContent)
    }
}
