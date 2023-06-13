package com.tokopedia.media.loader.di

import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.media.loader.StubInterceptor
import dagger.Component

@Component(
    modules = [
        MediaLoaderModule::class
    ]
)
@ApplicationScope
interface MediaLoaderComponent {
    fun inject(interceptor: StubInterceptor)
}
