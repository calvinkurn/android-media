package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.tokomart.search.di.SearchScope
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module
class GetSearchUseCaseModule {

    @SearchScope
    @Provides
    fun provideSearchFirstPageUseCase(): UseCase<SearchModel> {
        return GetSearchFirstPageUseCase()
    }
}