package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recommendation_widget_common.di.recomwidget.DaggerRecommendationComponent
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationComponent
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext

object RecommendationWidgetComponentProvider {


    private var component: RecommendationComponent? = null

    @VisibleForTesting
    fun setRecommendationComponent(component: RecommendationComponent) {
        this.component = component
    }

    fun getRecommendationComponent(context: Context): RecommendationComponent? {
        return if (component == null) {
            val appContext = context.getActivityFromContext()?.application
                as? BaseMainApplication ?: return null

            DaggerRecommendationComponent.builder()
                .baseAppComponent((appContext).baseAppComponent)
                .build()
        } else component
    }
}
