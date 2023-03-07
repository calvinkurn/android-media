package com.tokopedia.createpost.common.di

import com.tokopedia.createpost.common.view.service.SubmitPostService
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import dagger.Component

@CreatePostScope
@Component(modules = [
    CreatePostCommonModule::class,
    MediaUploaderModule::class,
])
interface CreatePostCommonComponent {

    fun inject(service: SubmitPostService)
}
