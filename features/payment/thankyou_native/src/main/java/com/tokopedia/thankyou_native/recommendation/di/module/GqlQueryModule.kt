package com.tokopedia.thankyou_native.recommendation.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.thankyou_native.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_RECOMMENDATION_DATA)
    fun provideRawRecommendationData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_thanks_recommendation)

    companion object {
        const val GQL_RECOMMENDATION_DATA = "gql_recommendation_data"
    }

}