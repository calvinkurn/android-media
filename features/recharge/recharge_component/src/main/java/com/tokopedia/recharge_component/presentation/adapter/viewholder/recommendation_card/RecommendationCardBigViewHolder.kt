package com.tokopedia.recharge_component.presentation.adapter.viewholder.recommendation_card

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.ViewRechargeRecommendationCardBigBinding
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_1
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_2
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_3

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
                tgExpiredRechargeRecommendationCardBig.text = recommendation.productExpired
                tgPriceRechargeRecommendationCardBig.text = recommendation.price

                tgSlashPriceRechargeRecommendationCardBig.run {
                    if (recommendation.slashPrice.isNotEmpty()) {
                        show()
                        text = recommendation.slashPrice
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else hide()
                }

                tgSpecialRecommendationCardBig.run {
                    if (recommendation.specialLabel.isNotEmpty()) {
                        show()
                        text = recommendation.specialLabel
                    } else hide()
                }

                labelDiscountRechargeRecommendationCardBig.run {
                    if (recommendation.discount.isNotEmpty()) {
                        show()
                        text = recommendation.discount
                    } else hide()
                }

                viewSeparatorTypeRechargeRecommendationCardBig.run {
                    if (recommendation.productExpired.isNotEmpty() &&
                        recommendation.productType.isNotEmpty()) {
                        tgExpiredRechargeRecommendationCardBig.setType(BODY_2)
                        tgProductTypeRechargeRecommendationCardBig.setType(BODY_1)
                        show()
                    } else {
                        tgExpiredRechargeRecommendationCardBig.setType(DISPLAY_3)
                        tgProductTypeRechargeRecommendationCardBig.setType(DISPLAY_3)
                        hide()
                    }
                }

                tgProductTypeRechargeRecommendationCardBig.run {
                    text = recommendation.productType
                    if (recommendation.productType.isNotEmpty() ||
                        recommendation.productExpired.isNotEmpty()) {
                        show()
                    } else {
                        hide()
                    }
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