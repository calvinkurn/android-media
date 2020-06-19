package com.tokopedia.withdraw.saldowithdrawal.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.withdraw.saldowithdrawal.constant.LocalCacheConstant
import com.tokopedia.withdraw.saldowithdrawal.di.scope.WithdrawScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by StevenFredian on 30/07/18.
 */
@Module
class WithdrawModule {


    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()


    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main


    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }


    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context?): LocalCacheHandler {
        return LocalCacheHandler(context, LocalCacheConstant.CACHE_FILE_NAME)
    }

}