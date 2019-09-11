package com.tokopedia.feedplus.profilerecommendation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedplus.profilerecommendation.view.fragment.FollowRecommendationFragment
import dagger.Component

/**
 * Created by jegul on 2019-09-11.
 */
@Component(
        modules = [FollowRecommendationModule::class],
        dependencies = [BaseAppComponent::class]
)
@FollowRecommendationScope
interface FollowRecommendationComponent {

    fun inject(fragment: FollowRecommendationFragment)
}