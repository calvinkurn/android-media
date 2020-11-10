package com.tokopedia.thankyou_native.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.thankyou_native.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_THANK_YOU_PAGE_DATA)
    fun provideRawThankYouPageData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_thanks_page_data)

    @Provides
    @Named(GQL_CHECK_WHITE_LIST)
    fun provideRawCheckWhitelist(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_thanks_white_list_rba)


    @Provides
    @Named(GQL_THANKS_MONTHLY_NEW_BUYER)
    fun provideMonthlyNewBuyerQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_thanks_monthly_new_buyer)

}