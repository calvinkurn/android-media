package com.tokopedia.feedplus.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.feedplus.detail.FeedDetailActivity
import dagger.Component

/**
 * Created by meyta.taliti on 07/09/23.
 */
@ActivityScope
@Component(
    modules = [
        FeedDetailModule::class,
        FeedDetailBindModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedDetailComponent {

    fun inject(activity: FeedDetailActivity)
}
