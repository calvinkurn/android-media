package com.tokopedia.merchantvoucher.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 03/04/19.
 */

@Module
class MerchantVoucherModule {

    @Provides
    @MerchantVoucherScope
    fun checkoutAnalyticsCartPage(@ApplicationContext context: Context): CheckoutAnalyticsCart {
        return CheckoutAnalyticsCart(context)
    }

    @Provides
    @MerchantVoucherScope
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @Provides
    @MerchantVoucherScope
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase =
            GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @MerchantVoucherScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}