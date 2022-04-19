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

    companion object {
        const val RECOMMENDATION_QUERY = "recQuery"
    }
}