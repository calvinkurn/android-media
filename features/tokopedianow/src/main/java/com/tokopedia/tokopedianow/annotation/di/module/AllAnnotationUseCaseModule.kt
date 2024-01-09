package com.tokopedia.tokopedianow.annotation.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.annotation.di.scope.AllAnnotationScope
import com.tokopedia.tokopedianow.annotation.domain.usecase.GetAllAnnotationPageUseCase
import dagger.Module
import dagger.Provides

@Module
class AllAnnotationUseCaseModule {

    @AllAnnotationScope
    @Provides
    fun provideGetAllAnnotationPageUseCase(graphqlRepository: GraphqlRepository): GetAllAnnotationPageUseCase {
        return GetAllAnnotationPageUseCase(graphqlRepository)
    }
}
