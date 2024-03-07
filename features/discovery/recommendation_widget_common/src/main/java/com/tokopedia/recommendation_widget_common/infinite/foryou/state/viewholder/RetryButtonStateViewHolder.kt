package com.tokopedia.recommendation_widget_common.infinite.foryou.state.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouRetryButtonBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.ParentRecommendationListener
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.RetryButtonStateModel
import com.tokopedia.utils.view.binding.viewBinding

class RetryButtonStateViewHolder constructor(
    view: View,
    private val listener: ParentRecommendationListener
) : AbstractViewHolder<RetryButtonStateModel>(view) {

    private val binding: WidgetForYouRetryButtonBinding? by viewBinding()

    override fun bind(element: RetryButtonStateModel) {
        binding?.homeRecomButtonRetry?.setOnClickListener {
            listener.onRetryGetNextProductRecommendationData()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_retry_button
    }
}
