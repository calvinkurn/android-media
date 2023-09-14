package com.tokopedia.recommendation_widget_common.infinite.main

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewModelDelegate
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext

class InfiniteRecommendationManager(
    private val context: Context
) : InfiniteRecommendationCallback {

    val adapter: InfiniteRecommendationAdapter by getAdapter()
    private val viewModel: InfiniteRecommendationViewModel? by getViewModel()

    private var productId: String = ""
    private var pageName: String = ""

    init {
        observeRecommendationProducts()
    }

    fun init(productId: String, pageName: String) {
        this.productId = productId
        this.pageName = pageName

        adapter.init()
        viewModel?.init()
    }

    private fun observeRecommendationProducts() {
        val lifecycleOwner = context as? LifecycleOwner ?: return
        viewModel?.components?.observe(lifecycleOwner) { components ->
            if (components.isEmpty()) {
                adapter.removeLoading()
            } else {
                adapter.appendComponents(components)
            }
        }
    }

    private fun getAdapter() = lazy {
        InfiniteRecommendationAdapter(this)
    }

    private fun getViewModel() = InfiniteRecommendationViewModelDelegate {
        context.getActivityFromContext()
    }

    override fun fetchRecommendation() {
        viewModel?.fetchComponents(productId, pageName)
    }
}
