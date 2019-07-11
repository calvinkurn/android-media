package com.tokopedia.topads.auto.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.auto.di.AutoAdsScope
import com.tokopedia.topads.auto.view.factory.AutoAdsWidgetViewModelFactory
import com.tokopedia.topads.auto.view.factory.DailyBudgetViewModelFactory
import com.tokopedia.topads.auto.view.factory.TopAdsInfoViewModelFactory
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers

/**
 * Author errysuprayogi on 16,May,2019
 */
@Module
@AutoAdsScope
class AutoAdsModule {

    @AutoAdsScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @AutoAdsScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @AutoAdsScope
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @AutoAdsScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @AutoAdsScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @AutoAdsScope
    @Provides
    fun provideDailyBudgetViewModelFactory(@ApplicationContext context: Context,
                                           dispatcher: CoroutineDispatcher,
                                           repository: GraphqlRepository,
                                           query: Map<String, String>):
            DailyBudgetViewModelFactory = DailyBudgetViewModelFactory(context, dispatcher, repository, query)

    @AutoAdsScope
    @Provides
    fun provideTopAdsInfoViewModelFactory(dispatcher: CoroutineDispatcher,
                                          repository: GraphqlRepository,
                                          query: Map<String, String>):
            TopAdsInfoViewModelFactory = TopAdsInfoViewModelFactory(dispatcher, repository, query)

    @AutoAdsScope
    @Provides
    fun provideAutoAdsWidgetViewModelFactory(dispatcher: CoroutineDispatcher,
                                          repository: GraphqlRepository,
                                          query: Map<String, String>):
            AutoAdsWidgetViewModelFactory = AutoAdsWidgetViewModelFactory(dispatcher, repository, query)

}