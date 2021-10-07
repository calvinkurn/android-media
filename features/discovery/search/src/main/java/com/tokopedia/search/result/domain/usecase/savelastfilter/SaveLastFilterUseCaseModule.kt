package com.tokopedia.search.result.domain.usecase.savelastfilter

import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.SAVE_LAST_FILTER_USE_CASE
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SaveLastFilterUseCaseModule {

    @Provides
    @SearchScope
    @Named(SAVE_LAST_FILTER_USE_CASE)
    fun provideSaveLastFilter(): UseCase<Int> =
        SaveLastFilterUseCase(GraphqlUseCase())
}