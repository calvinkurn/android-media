package com.tokopedia.withdraw.saldowithdrawal.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.withdraw.saldowithdrawal.di.scope.WithdrawScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by StevenFredian on 30/07/18.
 */
@WithdrawScope
@Module
class WithdrawModule {

    @WithdrawScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @WithdrawScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()


    @WithdrawScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main


    @WithdrawScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

}