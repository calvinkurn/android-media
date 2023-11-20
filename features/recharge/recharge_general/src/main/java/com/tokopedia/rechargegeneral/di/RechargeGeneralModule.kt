package com.tokopedia.rechargegeneral.di

import com.tokopedia.common.topupbills.analytics.CommonMultiCheckoutAnalytics
import com.tokopedia.rechargegeneral.model.mapper.RechargeGeneralMapper
import com.tokopedia.rechargegeneral.util.RechargeGeneralAnalytics
import dagger.Module
import dagger.Provides

/**
 * @author by resakemal on 07/01/20
 */
@Module
class RechargeGeneralModule {

    @RechargeGeneralScope
    @Provides
    fun provideRechargeGeneralAnalytics(): RechargeGeneralAnalytics = RechargeGeneralAnalytics()

    @RechargeGeneralScope
    @Provides
    fun provideRechargeGeneralMapper(): RechargeGeneralMapper = RechargeGeneralMapper()

    @RechargeGeneralScope
    @Provides
    fun provideAnalyticsCommonMultiCheckout(): CommonMultiCheckoutAnalytics {
        return CommonMultiCheckoutAnalytics()
    }
}
