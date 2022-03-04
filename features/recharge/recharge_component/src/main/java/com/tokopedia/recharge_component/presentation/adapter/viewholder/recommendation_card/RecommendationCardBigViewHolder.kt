package com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.listener.RechargeRecommendationListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardBigBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

class RecommendationCardBigViewHolder(
    private val recommendationTitle: String,
    private val recommendationListener: RechargeRecommendationCardListener,
    private val binding: ViewRechargeRecommendationCardBigBinding
) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: RecommendationCardWidgetModel, position: Int){
            with(binding){
                imgRechargeRecommendationCardBig.loadImage(recommendation.imageUrl)
                tgTitleRechargeRecommendationCardBig.text = recommendation.title
                tgProductTypeRechargeRecommendationCardBig.text = recommendation.productType
                tgExpiredRechargeRecommendationCardBig.text = recommendation.productExpired
                tgPriceRechargeRecommendationCardBig.text = recommendation.price

                tgSlashPriceRechargeRecommendationCardBig.run {
                    if (recommendation.slashPrice.isNotEmpty()) {
                        show()
                        text = recommendation.slashPrice
                    } else hide()
                }

                labelDiscountRechargeRecommendationCardBig.run {
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