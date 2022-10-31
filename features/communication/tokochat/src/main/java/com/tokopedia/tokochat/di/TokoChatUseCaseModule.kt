package com.tokopedia.tokochat.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.tokochat_common.util.TokoChatCacheManagerImpl
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModule {

    @TokoChatScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @TokoChatScope
    @Provides
    internal fun provideTopchatCacheManager(@TokoChatContext context: Context): TokoChatCacheManager {
        val topchatCachePref = context.getSharedPreferences("tokoChatCache", Context.MODE_PRIVATE)
        return TokoChatCacheManagerImpl(topchatCachePref)
    }
}
