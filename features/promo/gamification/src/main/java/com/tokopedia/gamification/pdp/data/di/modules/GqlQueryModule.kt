package com.tokopedia.gamification.pdp.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ActivityContextModule::class])
class GqlQueryModule {

    @GamificationPdpScope
    @Provides
    @Named(RECOMMENDATION_QUERY)
    fun provideRecommendationRawQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.recommendation_widget_common.R.raw.query_recommendation_widget)
    }

    @GamificationPdpScope
    @Provides
    @Named(GAMING_RECOMMENDATION_PARAM_QUERY)
    fun provideGamingRecommendationParamRawQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.gamification.R.raw.gami_recommend_params)
    }


    companion object {
        const val RECOMMENDATION_QUERY = "recQuery"
        const val GAMING_RECOMMENDATION_PARAM_QUERY = "gamingRecQuery"
    }
}