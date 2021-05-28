package com.tokopedia.tokomart.categorylist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.categorylist.di.scope.TokoMartCategoryListScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TokoMartCategoryListModule {

    @TokoMartCategoryListScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TokoMartCategoryListScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}