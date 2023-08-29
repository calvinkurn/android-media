package com.tokopedia.feedplus.browse.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.feedplus.browse.presentation.FeedBrowseActivity
import dagger.Component

/**
 * Created by meyta.taliti on 11/08/23.
 */
@ActivityScope
@Component(
    modules = [
        FeedBrowseModule::class,
        FeedBrowseViewModelModule::class,
        FeedBrowseRepositoryModule::class,
        FeedBrowseFragmentModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedBrowseComponent {

    fun inject(activity: FeedBrowseActivity)
}
