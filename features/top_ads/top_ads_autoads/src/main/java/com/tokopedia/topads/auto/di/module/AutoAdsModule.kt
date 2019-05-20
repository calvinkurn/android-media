package com.tokopedia.topads.auto.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.auto.data.network.datasource.AutoAdsNetworkDataSource
import com.tokopedia.topads.auto.data.network.datasource.AutoAdsNetworkDataSourceImpl
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoy
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoyImpl
import com.tokopedia.topads.auto.di.AutoAdsScope
import com.tokopedia.topads.auto.view.fragment.budget.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.fragment.budget.DailyBudgetViewModelFactory
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Author errysuprayogi on 16,May,2019
 */
@Module
@AutoAdsScope
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
    fun provideDataSource(gqlRepostory: GraphqlRepository, userSession: UserSession,
                          rawQueries: Map<String, String>): AutoAdsNetworkDataSource
            = AutoAdsNetworkDataSourceImpl(gqlRepostory, userSession, rawQueries)

    @AutoAdsScope
    @Provides
    fun provideAutoAdsRepository(dataSource: AutoAdsNetworkDataSource):
            AutoTopAdsRepositoy = AutoTopAdsRepositoyImpl(dataSource)

    @AutoAdsScope
    @Provides
    fun provideDailyBudgetViewModelFactory(@ApplicationContext context: Context,
                                           repositoy: AutoTopAdsRepositoy):
            DailyBudgetViewModelFactory = DailyBudgetViewModelFactory(context, repositoy)


    @AutoAdsScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}