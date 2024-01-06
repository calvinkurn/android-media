package com.tokopedia.tokopedianow.annotation.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics
import com.tokopedia.tokopedianow.annotation.di.scope.AllAnnotationScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @AllAnnotationScope
    @Provides
    fun provideAllAnnotationAnalytics(): AllAnnotationAnalytics {
        return AllAnnotationAnalytics()
    }
}
