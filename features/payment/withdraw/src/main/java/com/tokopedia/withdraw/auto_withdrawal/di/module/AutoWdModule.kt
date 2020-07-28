package com.tokopedia.withdraw.auto_withdrawal.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class AutoWdModule {
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main


    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

}