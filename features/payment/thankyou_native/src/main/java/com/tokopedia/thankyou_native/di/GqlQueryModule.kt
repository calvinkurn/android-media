package com.tokopedia.thankyou_native.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.RECOMMENDATION_DATA
import com.tokopedia.thankyou_native.THANK_YOU_PAGE_DATA
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(THANK_YOU_PAGE_DATA)
    fun provideRawThankYouPageData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_thanks_page_data)
    @Provides
    @Named(RECOMMENDATION_DATA)
    fun provideRawRecommendationData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_thanks_recommendation)

}