package com.tokopedia.catalog_library.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.catalog_library.usecase.CatalogLibraryUseCase
import com.tokopedia.catalog_library.usecase.CatalogListUseCase
import com.tokopedia.catalog_library.usecase.CatalogRelevantUseCase
import com.tokopedia.catalog_library.usecase.CatalogSpecialUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
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
    fun getCatalogSpecialUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): CatalogSpecialUseCase {
        return CatalogSpecialUseCase(graphqlRepository)
    }

    @CatalogLibraryScope
    @Provides
    fun getCatalogRelevantUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): CatalogRelevantUseCase {
        return CatalogRelevantUseCase(graphqlRepository)
    }

    @CatalogLibraryScope
    @Provides
    fun getCatalogListUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): CatalogListUseCase {
        return CatalogListUseCase(graphqlRepository)
    }

    @CatalogLibraryScope
    @Provides
    fun getCatalogLibraryUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): CatalogLibraryUseCase {
        return CatalogLibraryUseCase(graphqlRepository)
    }

    @CatalogLibraryScope
    @Provides
    fun providesTrackingQueue(@ApplicationContext context: Context): TrackingQueue = TrackingQueue(context)
}
