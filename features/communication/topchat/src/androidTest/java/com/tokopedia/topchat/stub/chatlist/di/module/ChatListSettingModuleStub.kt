package com.tokopedia.topchat.stub.chatlist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.stub.common.UserSessionStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object ChatListSettingModuleStub {

    @ChatListScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ChatListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    @ChatListScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }
}