package com.tokopedia.recommendation_widget_common.di.recomwidget

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by yfsx on 14/10/21.
 */
object RecommendationComponentInstance {

    private lateinit var recommendationComponent: RecommendationComponent

    fun getRecomWidgetComponent(application: Application): RecommendationComponent {
        if (!::recommendationComponent.isInitialized) {
            recommendationComponent = DaggerRecommendationComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent
            ).build()
        }
        return recommendationComponent
    }
}