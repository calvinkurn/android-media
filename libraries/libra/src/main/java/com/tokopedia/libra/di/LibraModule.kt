package com.tokopedia.libra.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.data.repository.CacheRepositoryImpl
import com.tokopedia.libra.domain.usecase.GetLibraCacheUseCase
import com.tokopedia.libra.domain.usecase.GetLibraRemoteUseCase
import dagger.Module
import dagger.Provides

@Module
class LibraModule {

    @Provides
    @LibraScope
    fun providesCacheRepository(@ApplicationContext context: Context): CacheRepository {
        return CacheRepositoryImpl(context, Gson())
    }

    @Provides
    @LibraScope
    fun providesGqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @LibraScope
    fun providesGetLibraRemoteUseCase(
        graphqlRepository: GraphqlRepository,
        cacheRepository: CacheRepository,
    ) = GetLibraRemoteUseCase(graphqlRepository, cacheRepository, CoroutineDispatchersProvider)

    @Provides
    @LibraScope
    fun providesGetLibraCacheUseCase(cacheRepository: CacheRepository) =
        GetLibraCacheUseCase(cacheRepository)

}
