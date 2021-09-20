package com.tokopedia.buyerorder.unifiedhistory.list.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 10/11/20.
 */
@Module
class UohListModule (val context: Context) {
    @UohListScope
    @Provides
    fun provideContext(): Context = context
    
    @UohListScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @UohListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @UohListScope
    @Provides
    fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return UserSession(context)
    }
}