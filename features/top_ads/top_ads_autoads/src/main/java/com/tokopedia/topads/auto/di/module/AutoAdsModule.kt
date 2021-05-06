package com.tokopedia.topads.auto.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.auto.di.AutoAdsScope
import com.tokopedia.topads.auto.view.factory.DailyBudgetViewModelFactory
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Author errysuprayogi on 16,May,2019
 */
@Module
class AutoAdsModule {

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
                                           dispatcher: CoroutineDispatchers,
                                           repository: GraphqlRepository,
                                           query: Map<String, String>,
                                           topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
                                           bidInfoUseCase: BidInfoUseCase):
            DailyBudgetViewModelFactory = DailyBudgetViewModelFactory(context, dispatcher, repository, query, topAdsGetShopDepositUseCase, bidInfoUseCase)
}