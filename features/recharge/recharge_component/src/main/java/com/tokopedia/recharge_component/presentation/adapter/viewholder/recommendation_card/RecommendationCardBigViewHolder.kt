package com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.listener.RechargeRecommendationListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardBigBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class RecommendationCardBigViewHolder(
    private val recommendationListener: RechargeRecommendationCardListener,
    private val binding: ViewRechargeRecommendationCardBigBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendationWidget: RecommendationCardWidgetModel){
            with(binding){
                imgRechargeRecommendationCardBig.loadImage(recommendationWidget.imageUrl)
                tgTitleRechargeRecommendationCardBig.text = recommendationWidget.title
                tgProductTypeRechargeRecommendationCardBig.text = recommendationWidget.productType
                tgExpiredRechargeRecommendationCardBig.text = recommendationWidget.productExpired
                tgPriceRechargeRecommendationCardBig.text = recommendationWidget.price

                root.setOnClickListener {
                    recommendationListener.onProductRecommendationCardClicked(recommendationWidget.appUrl)
                }
            }
        }
}