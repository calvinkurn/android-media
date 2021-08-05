package com.tokopedia.linkaccount.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.linkaccount.di.LinkAccountContext
import com.tokopedia.linkaccount.di.LinkAccountScope
import com.tokopedia.linkaccount.domain.GetLinkStatusUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module class LinkAccountModule(private val context: Context) {

    @Provides
    @LinkAccountContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @LinkAccountScope
    fun provideUserSession(
            @LinkAccountContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @LinkAccountScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @LinkAccountScope
    fun provideGetLinkStatusUseCase(repository: GraphqlRepository): GetLinkStatusUseCase {
        return GetLinkStatusUseCase(repository)
    }
}