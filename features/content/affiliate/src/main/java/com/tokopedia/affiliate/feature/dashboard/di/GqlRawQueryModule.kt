package com.tokopedia.affiliate.feature.dashboard.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@DashboardScope
@Module
class GqlRawQueryModule {

    @DashboardScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_AFF_PRODUCT_DETAIL)
    fun provideRawProductDetail(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_af_curated_product_detail)

    @DashboardScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_AFF_PRODUCT_TX_LIST)
    fun provideRawTxList(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_af_curated_product_tx_list)


}