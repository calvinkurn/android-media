package com.tokopedia.recommendation_widget_common.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetView

class RecommendationWidgetViewTestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommendation_widget_common_test_activity_layout)

        val pageName = intent.extras?.getString(PAGE_NAME_KEY) ?: ""

        findViewById<RecommendationWidgetView?>(R.id.recommendationWidgetView)?.bind(
            RecommendationWidgetModel(
                metadata = RecommendationWidgetMetadata(
                    pageName = pageName,
                )
            )
        )
    }

    companion object {
        const val PAGE_NAME_KEY = "PAGE_NAME"
    }
}
