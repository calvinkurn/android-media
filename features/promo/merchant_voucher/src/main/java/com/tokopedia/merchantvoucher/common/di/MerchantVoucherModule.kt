package com.tokopedia.merchantvoucher.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 03/04/19.
 */

@Module
class MerchantVoucherModule {

    @Provides
    @MerchantVoucherScope
    fun checkoutAnalyticsCartPage(): CheckoutAnalyticsCart {
        return CheckoutAnalyticsCart()
    }

    @Provides
    @MerchantVoucherScope
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources)
    }

}