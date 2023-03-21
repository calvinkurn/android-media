package com.tokopedia.createpost.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.createpost.common.view.service.SubmitPostService
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import dagger.Component

@CreatePostScope
@Component(
    modules = [
        CreatePostCommonModule::class,
        MediaUploaderModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface CreatePostCommonComponent {

    fun inject(service: SubmitPostService)
}
