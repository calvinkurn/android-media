package com.tokopedia.gamification.pdp.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gamification.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(RECOMMENDATION_QUERY)
    fun provideRecommendationRawQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }

    @Provides
    @Named(GAMING_RECOMMENDATION_PARAM_QUERY)
    fun provideGamingRecommendationParamRawQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }


    companion object {
        const val RECOMMENDATION_QUERY = "recQuery"
        const val GAMING_RECOMMENDATION_PARAM_QUERY = "gamingRecQuery"
    }
}