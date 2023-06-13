package com.tokopedia.recommendation_widget_common.widget.global

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext

internal class RecommendationWidgetViewModelDelegate(
    private val activity: () -> Activity?
): Lazy<RecommendationWidgetViewModel?> {

    private var recommendationViewModel: RecommendationWidgetViewModel? = null

    override fun isInitialized(): Boolean {
        return recommendationViewModel != null
    }

    override val value: RecommendationWidgetViewModel?
        get() {
            val act = activity.invoke() ?: return null
            return recommendationViewModel
                ?: initializeViewModel(act).also { recommendationViewModel = it }
        }

    private fun initializeViewModel(activity: Activity): RecommendationWidgetViewModel? {
        val component = RecommendationWidgetComponentProvider.getRecommendationComponent(activity)
            ?: return null
        val viewModelFactory = component.getViewModelFactory()
        val viewModelProvider = ViewModelProvider(activity as AppCompatActivity, viewModelFactory)
        return viewModelProvider[RecommendationWidgetViewModel::class.java]
    }
}

fun View.recommendationWidgetViewModel(): Lazy<RecommendationWidgetViewModel?> =
    RecommendationWidgetViewModelDelegate { this.context?.getActivityFromContext() }

fun Fragment.recommendationWidgetViewModel(): Lazy<RecommendationWidgetViewModel?> =
    RecommendationWidgetViewModelDelegate { this.context?.getActivityFromContext() }

fun Activity.recommendationWidgetViewModel(): Lazy<RecommendationWidgetViewModel?> =
    RecommendationWidgetViewModelDelegate { this }
