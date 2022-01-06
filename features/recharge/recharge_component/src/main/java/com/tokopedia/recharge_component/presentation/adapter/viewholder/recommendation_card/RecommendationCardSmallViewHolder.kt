package com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardSmallBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class RecommendationCardSmallViewHolder(
    private val recommendationListener: RechargeRecommendationCardListener,
    private val binding: ViewRechargeRecommendationCardSmallBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(recommendationWidget: RecommendationCardWidgetModel) {
        with(binding) {
            imgRechargeRecommendationCardSmall.loadImage(recommendationWidget.imageUrl)
            tgTitleRechargeRecommendationCardSmall.text = recommendationWidget.title
            tgPriceRechargeRecommendationCardSmall.text = recommendationWidget.price

            root.setOnClickListener {
                recommendationListener.onProductRecommendationCardClicked(recommendationWidget.appUrl)
            }
        }
    }
}