package com.tokopedia.minicart.common.widget.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class MiniCartWidgetModule {

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideResource(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    fun provideGraphqlRepository(@ApplicationContext graphqlRepository: GraphqlRepository): GraphqlRepository {
        return graphqlRepository
    }

}