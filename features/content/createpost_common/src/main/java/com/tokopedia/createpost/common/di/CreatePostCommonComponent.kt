package com.tokopedia.createpost.common.di

import com.tokopedia.createpost.common.view.service.SubmitPostServiceNew
import dagger.Component

@CreatePostScope
@Component(modules = [CreatePostCommonModule::class])
interface CreatePostCommonComponent {

    fun inject(service: SubmitPostServiceNew)
}