package com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardSmallBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class RecommendationCardSmallViewHolder(
    private val recommendationTitle: String,
    private val recommendationListener: RechargeRecommendationCardListener,
    private val binding: ViewRechargeRecommendationCardSmallBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(recommendation: RecommendationCardWidgetModel, position: Int) {
        with(binding) {
            imgRechargeRecommendationCardSmall.loadImage(recommendation.imageUrl)
            tgTitleRechargeRecommendationCardSmall.text = recommendation.title
            tgPriceRechargeRecommendationCardSmall.text = recommendation.price

            tgSlashPriceRechargeRecommendationCardSmall.run {
                if (recommendation.slashPrice.isNotEmpty()) {
                    show()
                    text = recommendation.slashPrice
                } else hide()
            }

            labelDiscountRechargeRecommendationCardSmall.run {
                if (recommendation.discount.isNotEmpty()) {
                    show()
                    text = recommendation.discount
                } else hide()
            }

            root.setOnClickListener {
                recommendationListener.onProductRecommendationCardClicked(
                    recommendationTitle,
                    recommendation,
                    position
                )
            }

            root.addOnImpressionListener(recommendation) {
                recommendationListener.onProductRecommendationCardImpression(
                    recommendation,
                    position
                )
            }
        }
    }
}