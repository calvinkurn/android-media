package com.tokopedia.promocheckoutmarketplace.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class PromoCheckoutModule {

    @PromoCheckoutMarketplaceScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @PromoCheckoutMarketplaceScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @PromoCheckoutMarketplaceScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @PromoCheckoutMarketplaceScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

}