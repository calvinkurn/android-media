package com.tokopedia.purchase_platform.features.one_click_checkout.order.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.purchase_platform.common.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.PreferenceListModelMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.GetOccCartGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.CheckoutOccGqlResponse
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@OrderSummaryPageScope
@Module(includes = [
    PeopleAddressNetworkModule::class,
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class]
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
}