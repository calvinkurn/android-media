package com.tokopedia.home.account.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.account.di.scope.SellerAccountScope
import com.tokopedia.home.account.presentation.util.dispatchers.AppDispatcherProvider
import com.tokopedia.home.account.presentation.util.dispatchers.DispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by alvinatin on 14/08/18.
 */
@Module
class SellerAccountModule {

    @SellerAccountScope
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @SellerAccountScope
    @Provides
    fun provideMainDispatcher(): DispatcherProvider {
        return AppDispatcherProvider()
    }

    @SellerAccountScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideUserSessionInterface(@SellerAccountScope context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}