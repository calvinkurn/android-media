package com.tokopedia.inbox.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.common.InboxCoroutineContextProvider
import com.tokopedia.inbox.common.InboxCoroutineDispatcher
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class InboxCommonModule {

    @InboxScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @InboxScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @InboxScope
    @Provides
    fun provideInboxCoroutineDispatcher(): InboxCoroutineDispatcher {
        return InboxCoroutineContextProvider()
    }

}