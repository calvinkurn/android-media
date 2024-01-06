package com.tokopedia.tokopedianow.annotation.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics
import com.tokopedia.tokopedianow.annotation.di.scope.AllAnnotationScope
import dagger.Module
import dagger.Provides

@Module
class AllAnnotationModule {

    @AllAnnotationScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @AllAnnotationScope
    @Provides
    fun provideAllAnnotationAnalytics(): AllAnnotationAnalytics {
        return AllAnnotationAnalytics()
    }
}
