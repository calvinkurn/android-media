package com.tokopedia.search.result.product.pagination

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class PaginationModule {

    @Binds
    @SearchScope
    abstract fun providePagination(paginationImpl: PaginationImpl): Pagination
}