package com.tokopedia.paylater.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.paylater.GQL_PAYLATER_ACTIVITY_DATA
import com.tokopedia.paylater.GQL_PAYLATER_APPLICATION_STATUS
import com.tokopedia.paylater.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_PAYLATER_ACTIVITY_DATA)
    fun provideRawPayLaterActivityData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_pay_later_activity_data)

    @Provides
    @Named(GQL_PAYLATER_APPLICATION_STATUS)
    fun provideRawPayLaterApplicationStatus(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_pay_later_application_status)

}