package com.tokopedia.topupbills.telco.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.topupbills.analytics.CommonMultiCheckoutAnalytics
import com.tokopedia.common.topupbills.data.source.ContactDataSource
import com.tokopedia.common.topupbills.data.source.ContactDataSourceImpl
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.utils.permission.PermissionCheckerHelper
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

    @DigitalTelcoScope
    @Provides
    fun provideContactDataSource(@ApplicationContext context: Context): ContactDataSource {
        return ContactDataSourceImpl(context.contentResolver)
    }
}
