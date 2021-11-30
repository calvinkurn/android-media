package com.tokopedia.recommendation_widget_common.viewutil

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recommendation_widget_common.di.recomwidget.DaggerRecommendationComponent
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetViewModel

/**
 * Created by yfsx on 13/10/21.
 */
class RecomWidgetViewModelDelegate<T : RecomWidgetViewModel>(val activity: () -> Activity?) :
    Lazy<T?> {

    private var recommendationViewModel: T? = null

    override fun isInitialized(): Boolean {
        return recommendationViewModel != null
    }

    override val value: T?
        get() {
            val act = activity.invoke() ?: return null
            return recommendationViewModel
                ?: initializeViewModel(act).also { recommendationViewModel = it }
        }

    private fun initializeViewModel(it: Activity): T {
        val appContext = it.applicationContext as BaseMainApplication
        val component = DaggerRecommendationComponent.builder().baseAppComponent(
            (appContext).baseAppComponent
        ).build()
        component.inject(appContext)
        val viewModelFactory = component.getViewModelFactory()
        val viewModelProvider = ViewModelProvider(it as AppCompatActivity, viewModelFactory)
        return viewModelProvider[RecomWidgetViewModel::class.java] as T
    }
}