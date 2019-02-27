package com.tokopedia.feedcomponent.view.viewmodel.banner

/**
 * @author by milhamj on 10/01/19.
 */
data class TrackingBannerModel(
        val templateType: String = "",
        val activityName: String = "",
        val trackingType: String = "",
        val mediaType: String = "",
        val tagsType: String = "",
        val bannerUrl: String = "",
        val postId: Int = 0,
        val totalBanner: Int = 0,
        val bannerPosition: Int = 0
)