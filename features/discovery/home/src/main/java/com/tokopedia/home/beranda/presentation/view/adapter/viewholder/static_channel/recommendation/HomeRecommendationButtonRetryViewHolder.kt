package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationButtonRetryUiModel
import com.tokopedia.home.databinding.ItemHomeRecommendationButtonRetryBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener

class HomeRecommendationButtonRetryViewHolder(
    view: View,
    private val homeRecommendationListener: GlobalRecomListener
): AbstractViewHolder<HomeRecommendationButtonRetryUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_home_recommendation_button_retry
    }

    private val binding = ItemHomeRecommendationButtonRetryBinding.bind(itemView)
    override fun bind(element: HomeRecommendationButtonRetryUiModel?) {
        binding.homeRecomButtonRetry.setOnClickListener {
            homeRecommendationListener.onRetryGetNextProductRecommendationData()
        }
    }
}
