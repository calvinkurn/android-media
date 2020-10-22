package com.tokopedia.topupbills.searchnumber.di

import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import dagger.Module
import dagger.Provides

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@Module
class DigitalTelcoSearchModule {

    @DigitalTelcoSearchScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @DigitalTelcoSearchScope
    @Provides
    fun provideDigitalTopupAnalytics(): DigitalTopupAnalytics {
        return DigitalTopupAnalytics()
    }
}