package com.tokopedia.search.result.product.responsecode

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class ResponseCodeProviderModule {

    @Binds
    @SearchScope
    abstract fun provideResponseCodeProvider(responseCodeImpl: ResponseCodeImpl): ResponseCodeProvider
}
