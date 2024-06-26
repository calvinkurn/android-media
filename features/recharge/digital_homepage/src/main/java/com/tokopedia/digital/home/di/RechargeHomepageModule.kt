package com.tokopedia.digital.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.home.analytics.RechargeHomepageAnalytics
import com.tokopedia.digital.home.domain.DigitalHomepageSearchByDynamicIconUseCase
import com.tokopedia.digital.home.domain.DigitalPersoCloseWidgetUseCase
import com.tokopedia.digital.home.domain.SearchAutoCompleteHomePageUseCase
import com.tokopedia.digital.home.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.presentation.monitoring.callback.RechargeHomepagePerformanceCallback
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Job

@Module
class RechargeHomepageModule {

    @RechargeHomepageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @RechargeHomepageScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @RechargeHomepageScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @RechargeHomepageScope
    @Provides
    fun provideSearchCategoryUseCase(graphqlRepository: GraphqlRepository): SearchCategoryHomePageUseCase =
            SearchCategoryHomePageUseCase(graphqlRepository)

    @RechargeHomepageScope
    @Provides
    fun provideSearchLocalUseCase(graphqlRepository: GraphqlRepository): DigitalHomepageSearchByDynamicIconUseCase =
            DigitalHomepageSearchByDynamicIconUseCase(graphqlRepository)

    @RechargeHomepageScope
    @Provides
    fun provideSearchAutoCompleteUseCase(graphqlRepository: GraphqlRepository): SearchAutoCompleteHomePageUseCase =
            SearchAutoCompleteHomePageUseCase(graphqlRepository)

    @RechargeHomepageScope
    @Provides
    fun provideDigitalClosePersoWidgetUseCase(graphqlRepository: GraphqlRepository): DigitalPersoCloseWidgetUseCase =
        DigitalPersoCloseWidgetUseCase(graphqlRepository)

    @RechargeHomepageScope
    @Provides
    fun provideRechargeHomepageTracking(): RechargeHomepageAnalytics = RechargeHomepageAnalytics()

    @RechargeHomepageScope
    @Provides
    fun provideCoroutineJob(): Job = Job()

    @RechargeHomepageScope
    @Provides
    fun provideRechargeHomepagePerformanceCallback(): RechargeHomepagePerformanceCallback {
        return RechargeHomepagePerformanceCallback()
    }

}
