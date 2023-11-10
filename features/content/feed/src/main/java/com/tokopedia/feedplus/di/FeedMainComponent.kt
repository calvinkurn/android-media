package com.tokopedia.feedplus.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.comment.di.ContentCommentModule
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponent
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import dagger.Component

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@FeedMainScope
@Component(
    modules = [
        FeedMainModule::class,
        FeedMainViewModelModule::class,
        FeedFragmentModule::class,
        ContentCommentModule::class
    ],
    dependencies = [
        BaseAppComponent::class,
        CreationUploaderComponent::class,
    ]
)
interface FeedMainComponent {
    fun inject(feedBaseFragment: FeedBaseFragment)
    fun inject(feedFragment: FeedFragment)
}
