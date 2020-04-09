package com.tokopedia.exploreCategory.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.exploreCategory.ECConstants.Companion.gql_ec_dynamic_home_icon_query
import com.tokopedia.exploreCategory.R
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ECModule {

    @Named(gql_ec_dynamic_home_icon_query)
    @ECScope
    @Provides
    fun provideECDynamicHomeIconQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_ec_dynamic_home_icon_query)
    }

    @Provides
    @ECScope
    fun providesTrackingQueue(@ApplicationContext context: Context): TrackingQueue = TrackingQueue(context)
}
