package com.tokopedia.entertainment.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModelFactory
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Author errysuprayogi on 06,February,2020
 */

@EventHomeScope
@Module
class EventHomeModule {

    @EventHomeScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @EventHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @EventHomeScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @EventHomeScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @EventHomeScope
    @Provides
    fun provideViewModelFactory(@ApplicationContext context: Context,
                                dispatcher: CoroutineDispatcher,
                                repository: GraphqlRepository,
                                userSession: UserSessionInterface):
            HomeEventViewModelFactory = HomeEventViewModelFactory(context, dispatcher, repository, userSession)

}