package com.tokopedia.catalog_library.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class CatalogLibraryUseCaseModule {

    @CatalogLibraryScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @CatalogLibraryScope
    @Provides
    fun provideRxGQLUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CatalogLibraryScope
    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @CatalogLibraryScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @CatalogLibraryScope
    @Provides
    fun providesTrackingQueue(@ApplicationContext context: Context): TrackingQueue = TrackingQueue(context)

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}
