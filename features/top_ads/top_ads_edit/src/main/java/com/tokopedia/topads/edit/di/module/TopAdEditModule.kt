package com.tokopedia.topads.edit.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.topads.edit.di.EditAdScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Pika on 4/4/20.
 */


@Module
class TopAdEditModule(val context: Context) {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository


   @Provides
   @ActivityContext
   fun provideActivityContext() = context

}