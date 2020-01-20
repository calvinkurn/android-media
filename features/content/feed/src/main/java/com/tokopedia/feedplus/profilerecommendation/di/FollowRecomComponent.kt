package com.tokopedia.feedplus.profilerecommendation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedplus.profilerecommendation.view.fragment.FollowRecomFragment
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * Created by jegul on 2019-09-11.
 */
@Component(
        modules = [FollowRecomModule::class],
        dependencies = [BaseAppComponent::class]
)
@FollowRecomScope
interface FollowRecomComponent {

    fun inject(fragment: FollowRecomFragment)

    fun providesUserSessionInterface(): UserSessionInterface
}