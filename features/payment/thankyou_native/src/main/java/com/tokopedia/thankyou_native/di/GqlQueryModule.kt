package com.tokopedia.thankyou_native.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.thankyou_native.GQL_CHECK_WHITE_LIST
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.GQL_RECOMMENDATION_DATA
import com.tokopedia.thankyou_native.GQL_THANK_YOU_PAGE_DATA
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
    @Named(GQL_RECOMMENDATION_DATA)
    fun provideRawRecommendationData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_thanks_recommendation)

    @Provides
    @Named(GQL_CHECK_WHITE_LIST)
    fun provideRawCheckWhitelist(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_thanks_white_list_rba)

}