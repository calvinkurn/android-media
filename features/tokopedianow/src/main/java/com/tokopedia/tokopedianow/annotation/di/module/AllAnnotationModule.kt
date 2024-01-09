package com.tokopedia.tokopedianow.annotation.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics
import com.tokopedia.tokopedianow.annotation.di.scope.AllAnnotationScope
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import dagger.Module
import dagger.Provides

@Module
class AllAnnotationModule(
    val categoryId: String,
    val annotationType: String
) {
    @AllAnnotationScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @AllAnnotationScope
    @Provides
    fun provideTokoNowLocalAddress(
        @ApplicationContext context: Context
    ): TokoNowLocalAddress = TokoNowLocalAddress(context)

    @AllAnnotationScope
    @Provides
    fun provideAllAnnotationAnalytics(): AllAnnotationAnalytics = AllAnnotationAnalytics(
        categoryId = categoryId,
        annotationType = annotationType
    )
}
