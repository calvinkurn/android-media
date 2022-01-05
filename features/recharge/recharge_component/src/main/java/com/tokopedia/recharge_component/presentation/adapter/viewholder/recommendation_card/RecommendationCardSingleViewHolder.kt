package com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardSingleBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class RecommendationCardSingleViewHolder(
    private val recommendationListener: RechargeRecommendationCardListener,
    private val binding: ViewRechargeRecommendationCardSingleBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(recommendationWidget: RecommendationCardWidgetModel) {
        with(binding) {
            imgRechargeRecommendationCardSingle.loadImage(recommendationWidget.imageUrl)
            tgTitleRechargeRecommendationCardSingle.text = recommendationWidget.title
            tgDueDateRechargeRecommendationCardSingle.text = recommendationWidget.dueDate
            tgPriceRechargeRecommendationCardSingle.text = recommendationWidget.price

            root.setOnClickListener {
                recommendationListener.onProductRecommendationCardClicked(recommendationWidget.appUrl)
            }
        }
    }
}