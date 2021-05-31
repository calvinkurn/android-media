package com.tokopedia.oneclickcheckout.order.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.oneclickcheckout.common.OVO_ACTIVATION_URL
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCaseImpl
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceModelMapper
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.checkout.CheckoutOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.get.GetOccCartGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [PeopleAddressNetworkModule::class, PurchasePlatformNetworkModule::class])
open class OrderSummaryPageModule(private val activity: Activity) {

    @OrderSummaryPageScope
    @Provides
    fun provideContext(): Context = activity

    @OrderSummaryPageScope
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = MainScheduler()

    @OrderSummaryPageScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @OrderSummaryPageScope
    @Provides
    fun provideGetOccCartGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetOccCartGqlResponse> = GraphqlUseCase(graphqlRepository)

    @OrderSummaryPageScope
    @Provides
    fun providesGetPreferenceListUseCase(graphqlRepository: GraphqlRepository): GetPreferenceListUseCase {
        return GetPreferenceListUseCaseImpl(GraphqlUseCase(graphqlRepository), PreferenceModelMapper)
    }

    @OrderSummaryPageScope
    @Provides
    fun provideGetAddressCornerUseCase(context: Context, graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase, mapper: AddressCornerMapper): GetAddressCornerUseCase {
        return GetAddressCornerUseCase(context, graphqlUseCase, mapper)
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

    @OrderSummaryPageScope
    @Provides
    fun provideClearCacheAutoApplyStackUseCase(context: Context): ClearCacheAutoApplyStackUseCase {
        return ClearCacheAutoApplyStackUseCase(context)
    }

    @OrderSummaryPageScope
    @Provides
    fun provideValidateUsePromoRevampUseCase(context: Context,
                                             graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase,
                                             chosenAddressRequestHelper: ChosenAddressRequestHelper): ValidateUsePromoRevampUseCase {
        return ValidateUsePromoRevampUseCase(context, graphqlUseCase, chosenAddressRequestHelper)
    }

    @OrderSummaryPageScope
    @Provides
    fun provideGetRatesUseCase(context: Context, converter: ShippingDurationConverter,
                               schedulerProvider: SchedulerProvider): GetRatesUseCase {
        return GetRatesUseCase(context, converter, schedulerProvider)
    }

    @OrderSummaryPageScope
    @Provides
    @Named(AtcConstant.MUTATION_ATC_OCC_EXTERNAL)
    fun provideAtcOccExternalMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_checkout_external)
    }

    @OrderSummaryPageScope
    @Provides
    @Named(OVO_ACTIVATION_URL)
    open fun provideOvoActivationLink(): String {
        return "${TokopediaUrl.getInstance().WEB}ovo/api/v2/activate"
    }

}