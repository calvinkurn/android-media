package com.tokopedia.recharge_pdp_emoney.di

import com.tokopedia.common.topupbills.analytics.CommonMultiCheckoutAnalytics
import dagger.Module
import dagger.Provides

/**
 * @author by jessica on 29/03/21
 */

@Module
class EmoneyPdpModule {

    @EmoneyPdpScope
    @Provides
    fun provideAnalyticsCommonMultiCheckout(): CommonMultiCheckoutAnalytics {
        return CommonMultiCheckoutAnalytics()
    }
}
