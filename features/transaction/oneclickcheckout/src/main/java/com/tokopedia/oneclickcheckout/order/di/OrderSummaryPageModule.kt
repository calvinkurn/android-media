package com.tokopedia.oneclickcheckout.order.di

import android.app.Activity
import android.content.Context
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.oneclickcheckout.common.OCC_OVO_ACTIVATION_URL
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(
    includes = [
        PurchasePlatformNetworkModule::class,
        PurchasePlatformBaseModule::class
    ]
)
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
    fun provideGetAddressCornerUseCase(
        context: Context,
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase,
        mapper: AddressCornerMapper
    ): GetAddressCornerUseCase {
        return GetAddressCornerUseCase(context, graphqlUseCase, mapper)
    }

    @OrderSummaryPageScope
    @Provides
    @Named(OCC_OVO_ACTIVATION_URL)
    open fun provideOvoActivationLink(): String {
        return "${TokopediaUrl.getInstance().WEB}ovo/api/v2/activate"
    }

    @Provides
    @OrderSummaryPageScope
    fun provideEPharmacyAnalytics(userSession: UserSessionInterface): EPharmacyAnalytics {
        return EPharmacyAnalytics(userSession.userId)
    }
}
