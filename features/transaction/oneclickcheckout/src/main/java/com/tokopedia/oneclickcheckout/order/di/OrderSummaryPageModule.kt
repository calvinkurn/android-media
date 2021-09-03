package com.tokopedia.oneclickcheckout.order.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.oneclickcheckout.common.OCC_OVO_ACTIVATION_URL
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

    // Provide activity context to prevent DF issue
    @OrderSummaryPageScope
    @Provides
    fun provideGetAddressCornerUseCase(context: Context, graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase, mapper: AddressCornerMapper): GetAddressCornerUseCase {
        return GetAddressCornerUseCase(context, graphqlUseCase, mapper)
    }

    @OrderSummaryPageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
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
    @Named(OCC_OVO_ACTIVATION_URL)
    open fun provideOvoActivationLink(): String {
        return "${TokopediaUrl.getInstance().WEB}ovo/api/v2/activate"
    }
}