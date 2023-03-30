package com.tokopedia.feedplus.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.play_common.shortsuploader.di.uploader.PlayShortsUploaderModule
import dagger.Component

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@FeedMainScope
@Component(
    modules = [
        FeedMainModule::class,
        FeedMainViewModelModule::class,
        PlayShortsUploaderModule::class,
    ], dependencies = [BaseAppComponent::class]
)
interface FeedMainComponent {
    fun inject(feedBaseFragment: FeedBaseFragment)
    fun inject(feedFragment: FeedFragment)
}
