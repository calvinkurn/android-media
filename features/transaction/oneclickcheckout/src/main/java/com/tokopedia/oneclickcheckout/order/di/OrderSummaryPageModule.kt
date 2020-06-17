package com.tokopedia.oneclickcheckout.order.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceListModelMapper
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.GetOccCartGqlResponse
import com.tokopedia.oneclickcheckout.order.data.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.checkout.CheckoutOccGqlResponse
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@OrderSummaryPageScope
@Module(includes = [PeopleAddressNetworkModule::class, PurchasePlatformNetworkModule::class]
)
class OrderSummaryPageModule {

    @OrderSummaryPageScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @OrderSummaryPageScope
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = MainScheduler()

    @OrderSummaryPageScope
    @Provides
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @OrderSummaryPageScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @OrderSummaryPageScope
    @Provides
    fun provideGetOccCartGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetOccCartGqlResponse> = GraphqlUseCase(graphqlRepository)

    @OrderSummaryPageScope
    @Provides
    fun providesGetPreferenceListUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, preferenceListModelMapper: PreferenceListModelMapper): GetPreferenceListUseCase {
        return GetPreferenceListUseCase(context, GraphqlUseCase(graphqlRepository), preferenceListModelMapper)
    }

    @OrderSummaryPageScope
    @Provides
    fun providesUpdateCartOccGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<UpdateCartOccGqlResponse> = GraphqlUseCase(graphqlRepository)

    @OrderSummaryPageScope
    @Provides
    fun providesCheckoutOccGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<CheckoutOccGqlResponse> = GraphqlUseCase(graphqlRepository)

    @OrderSummaryPageScope
    @Provides
    fun provideOrderSummaryAnalytics(): OrderSummaryAnalytics {
        return OrderSummaryAnalytics()
    }

    @OrderSummaryPageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}