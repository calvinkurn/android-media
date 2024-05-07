package com.tokopedia.libra.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.data.repository.CacheRepositoryImpl
import com.tokopedia.libra.domain.SetLibraUseCase
import dagger.Module
import dagger.Provides

@Module
class LibraModule {

    @Provides
    @LibraScope
    fun providesCacheRepository(@ApplicationContext context: Context): CacheRepository {
        return CacheRepositoryImpl(context)
    }

    @Provides
    @LibraScope
    fun providesGetLibraUseCase(
        graphqlRepository: GraphqlRepository,
        cacheRepository: CacheRepository,
        @ApplicationContext dispatchers: CoroutineDispatchers
    ) = SetLibraUseCase(graphqlRepository, cacheRepository, dispatchers)
}
