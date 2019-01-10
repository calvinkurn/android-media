package com.tokopedia.feedcomponent.view.viewmodel.recommendation

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateItem

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
        val redirectUrl: String = "",
        val cta: FollowCta = FollowCta(),
        val template: TemplateItem = TemplateItem(),
        val trackingRecommendationModel: TrackingRecommendationModel = TrackingRecommendationModel()
)