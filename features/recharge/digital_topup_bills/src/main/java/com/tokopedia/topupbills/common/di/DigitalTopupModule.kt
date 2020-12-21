package com.tokopedia.topupbills.common.di

import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import dagger.Module
import dagger.Provides

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@Module
class DigitalTopupModule {

    @DigitalTopupScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @DigitalTopupScope
    @Provides
    fun provideDigitalTopupAnalytics(): DigitalTopupAnalytics {
        return DigitalTopupAnalytics()
    }
}