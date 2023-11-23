package com.tokopedia.search.result.product.deduplication

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class DeduplicationModule {

    @SearchScope
    @Binds
    abstract fun provideDeduplicationView(view: DeduplicationViewDelegate): DeduplicationView
}
