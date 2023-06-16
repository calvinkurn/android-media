package com.tokopedia.recommendation_widget_common.widget.global

import dagger.Module
import dagger.Provides

@Module
class RecommendationWidgetStateModule {

    @Provides
    fun providesRecommendationWidgetState(): RecommendationWidgetState {
        return RecommendationWidgetState()
    }
}
