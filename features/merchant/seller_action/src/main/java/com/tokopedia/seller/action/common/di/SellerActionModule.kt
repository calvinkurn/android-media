package com.tokopedia.seller.action.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcher
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@SellerActionScope
@Module
class SellerActionModule {

    @SellerActionScope
    @Provides
    fun provideGraphqlRepository(@ApplicationContext context: Context): GraphqlRepository {
        GraphqlClient.init(context)
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerActionScope
    @Provides
    fun provideDispatchers(): SellerActionDispatcherProvider = SellerActionDispatcher

    @SellerActionScope
    @Provides
    fun providerUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

}