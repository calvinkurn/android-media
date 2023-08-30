package com.tokopedia.recommendation_widget_common.infinite.main.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationViewModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetComponentProvider

class InfiniteRecommendationViewModelDelegate(
    private val getActivity: () -> Activity?
) : Lazy<InfiniteRecommendationViewModel?> {

    private var viewModel: InfiniteRecommendationViewModel? = null
    override val value: InfiniteRecommendationViewModel?
        get() {
            val activity = getActivity.invoke() ?: return null
            return viewModel ?: initializeViewModel(activity).also { viewModel = it }
        }

    override fun isInitialized(): Boolean {
        return viewModel != null
    }

    private fun initializeViewModel(activity: Activity): InfiniteRecommendationViewModel? {
        val component = RecommendationWidgetComponentProvider.getRecommendationComponent(activity)
            ?: return null
        val viewModelFactory = component.getViewModelFactory()
        val viewModelProvider = ViewModelProvider(activity as AppCompatActivity, viewModelFactory)
        return viewModelProvider[InfiniteRecommendationViewModel::class.java]
    }
}
