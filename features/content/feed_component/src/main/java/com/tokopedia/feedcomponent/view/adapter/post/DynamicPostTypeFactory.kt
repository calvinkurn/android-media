package com.tokopedia.feedcomponent.view.adapter.post

import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicPostTypeFactory {
    fun type(dynamicPostViewModel: DynamicPostViewModel): Int

    fun type(feedRecommendationViewModel: FeedRecommendationViewModel): Int
}