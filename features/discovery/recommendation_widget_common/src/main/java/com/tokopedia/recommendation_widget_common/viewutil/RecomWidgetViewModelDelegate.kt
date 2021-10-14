package com.tokopedia.recommendation_widget_common.viewutil

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recommendation_widget_common.di.recomwidget.DaggerRecommendationComponent
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationWidgetModule
import com.tokopedia.recommendation_widget_common.presenter.RecommendationViewModel

/**
 * Created by yfsx on 13/10/21.
 */
class RecomWidgetViewModelDelegate<T : RecommendationViewModel>(val context: () -> Context) :
    Lazy<T> {

    private var recommendationViewModel: T? = null

    override fun isInitialized(): Boolean {
        return recommendationViewModel != null
    }

    override val value: T
        get() = recommendationViewModel ?: initializeViewModel(context.invoke())!!

    private fun initializeViewModel(it: Context): T? {
        val component = DaggerRecommendationComponent.builder()
            .recommendationWidgetModule(RecommendationWidgetModule())
            .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(it.applicationContext as BaseMainApplication)
        val viewModelFactory = component.getViewModelFactory()
        return when (it) {
            is AppCompatActivity -> {
                val viewModelProvider = ViewModelProvider(it, viewModelFactory)
                viewModelProvider[RecommendationViewModel::class.java] as T
            }
            is ContextThemeWrapper -> {
                val activity = it.getActivityFromContext()
                activity?.let {
                    if (activity is AppCompatActivity) {
                        val viewModelProvider = ViewModelProvider(activity, viewModelFactory)
                        viewModelProvider[RecommendationViewModel::class.java] as T
                    } else {
                        null
                    }
                }
            }
            else -> {
                null
            }
        }
    }
}