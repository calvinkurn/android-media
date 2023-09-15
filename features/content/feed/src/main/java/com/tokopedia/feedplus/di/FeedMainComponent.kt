package com.tokopedia.feedplus.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.comment.di.ContentCommentModule
import com.tokopedia.creation.common.di.ContentCreationViewModelModule
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.play_common.shortsuploader.di.uploader.PlayShortsUploaderModule
import dagger.Component

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@Component(
    modules = [
        FeedMainModule::class,
        FeedMainViewModelModule::class,
        PlayShortsUploaderModule::class,
        FeedFragmentModule::class,
        ContentCommentModule::class,
        ContentCreationViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedMainComponent {
    fun inject(feedBaseFragment: FeedBaseFragment)
    fun inject(feedFragment: FeedFragment)
}
