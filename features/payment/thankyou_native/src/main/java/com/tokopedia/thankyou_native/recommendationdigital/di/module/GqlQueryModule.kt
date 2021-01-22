package com.tokopedia.thankyou_native.recommendationdigital.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.thankyou_native.GQL_DIGITAL_RECOMMENDATION
import com.tokopedia.thankyou_native.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_DIGITAL_RECOMMENDATION)
    fun provideDigitalRecommendationData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.typ_digital_recommendation_list)


}