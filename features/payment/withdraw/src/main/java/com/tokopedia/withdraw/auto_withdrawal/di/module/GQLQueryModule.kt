package com.tokopedia.withdraw.auto_withdrawal.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.QUERY_GET_AUTO_WD_STATUS
import com.tokopedia.withdraw.auto_withdrawal.QUERY_GET_INFO_AUTO_WD
import com.tokopedia.withdraw.auto_withdrawal.QUERY_GET_TNC_AUTO_WD
import com.tokopedia.withdraw.auto_withdrawal.QUERY_UPSERT_AUTO_WD_DATA
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GQLQueryModule {

    @Provides
    @Named(QUERY_GET_AUTO_WD_STATUS)
    fun provideRawTGetAutoWDStatus(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.swd_query_get_auto_wd_status)

    @Provides
    @Named(QUERY_GET_INFO_AUTO_WD)
    fun provideRawGetInfoAutoWD(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_info_auto_wd)

    @Provides
    @Named(QUERY_GET_TNC_AUTO_WD)
    fun provideRawGetTNCAutoWD(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_tnc_auto_wd)

    @Provides
    @Named(QUERY_UPSERT_AUTO_WD_DATA)
    fun provideRawUpsertAutoWDData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_upsert_auto_wd_data)

}