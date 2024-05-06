package com.tokopedia.feedplus.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponent
import com.tokopedia.feed.common.comment.di.ContentCommentModule
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import dagger.BindsInstance
import dagger.Component

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@FeedMainScope
@Component(
    modules = [
        FeedMainModule::class,
        FeedBindModule::class,
        FeedMainViewModelModule::class,
        FeedFragmentModule::class,
        ContentCommentModule::class
    ],
    dependencies = [
        BaseAppComponent::class,
        CreationUploaderComponent::class
    ]
)
interface FeedMainComponent {
    fun inject(feedBaseFragment: FeedBaseFragment)
    fun inject(feedFragment: FeedFragment)

    @Component.Factory
    interface Factory {
        fun build(
            appComponent: BaseAppComponent,
            creationUploaderComponent: CreationUploaderComponent,
            @BindsInstance activityContext: Context
        ): FeedMainComponent
    }
}
