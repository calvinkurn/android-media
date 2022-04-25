package com.tokopedia.shopadmin.feature.redirection.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shopadmin.feature.redirection.di.scope.ShopAdminRedirectionScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopAdminRedirectionViewModelModule::class])
class ShopAdminRedirectionModule {

    @ShopAdminRedirectionScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopAdminRedirectionScope
    @Provides
    fun provideUserGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}