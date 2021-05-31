package com.tokopedia.tokomart.searchcategory.domain.usecase

import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module
class GetProductCountUseCaseModule {

    @Provides
    fun provideGetProductCountUseCase(): UseCase<String> {
        return GetProductCountUseCase()
    }
}