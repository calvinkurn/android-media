package com.tokopedia.seller.search.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.search.common.di.scope.GlobalSearchSellerScope
import com.tokopedia.seller.search.common.util.GlobalSearchConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@GlobalSearchSellerScope
class GlobalSearchSellerModule {

    @GlobalSearchSellerScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @GlobalSearchSellerScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @GlobalSearchSellerScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @GlobalSearchSellerScope
    @Provides
    fun provideMultipleRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @GlobalSearchSellerScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @GlobalSearchSellerScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
            FirebaseRemoteConfigImpl(context)

    @GlobalSearchSellerScope
    @Provides
    fun provideGlobalSearchRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): GlobalSearchConfig {
        return GlobalSearchConfig(remoteConfig)
    }
}