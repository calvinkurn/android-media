package com.tokopedia.sellerhome.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.data.remote.TickerService
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@SellerHomeScope
@Module
class SellerHomeModule {

    @SellerHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerHomeScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @SellerHomeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @SellerHomeScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SellerHomeScope
    @Provides
    fun provideTickerRetrofit(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl(TickerService.BASE_URL).build()
    }

    @SellerHomeScope
    @Provides
    fun provideTickerService(retrofit: Retrofit): TickerService {
        return retrofit.create(TickerService::class.java)
    }

    @SellerHomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
            FirebaseRemoteConfigImpl(context)
}