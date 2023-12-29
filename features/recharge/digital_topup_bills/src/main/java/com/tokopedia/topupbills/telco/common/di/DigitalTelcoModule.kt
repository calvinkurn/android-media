package com.tokopedia.topupbills.telco.common.di

import com.tokopedia.common.topupbills.analytics.CommonMultiCheckoutAnalytics
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import dagger.Module
import dagger.Provides

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@Module
class DigitalTelcoModule {

    @DigitalTelcoScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @DigitalTelcoScope
    @Provides
    fun provideDigitalTopupAnalytics(): DigitalTopupAnalytics {
        return DigitalTopupAnalytics()
    }

    @DigitalTelcoScope
    @Provides
    fun provideAnalyticsCommonMultiCheckout(): CommonMultiCheckoutAnalytics {
        return CommonMultiCheckoutAnalytics()
    }
}
