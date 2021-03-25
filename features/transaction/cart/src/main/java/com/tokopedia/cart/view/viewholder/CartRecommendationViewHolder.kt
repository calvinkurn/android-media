package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecommendationBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartRecommendationViewHolder(private val binding: ItemCartRecommendationBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_cart_recommendation
    }

    internal var isTopAds = false

    fun bind(element: CartRecommendationItemHolderData) {
        binding.productCardView.apply {
            setProductModel(
                    element.recommendationItem.toProductCardModel(true, UnifyButton.Type.MAIN)
            )
            setOnClickListener {
                actionListener?.onRecommendationProductClicked(
                        element.recommendationItem
                )
            }
            setAddToCartOnClickListener {
                actionListener?.onButtonAddToCartClicked(element)
            }

            setImageProductViewHintListener(
                    element.recommendationItem,
                    object : ViewHintListener {
                        override fun onViewHint() {
                            actionListener?.onRecommendationProductImpression(
                                    element.recommendationItem
                            )
                        }
                    }
            )
        }

        if (!element.hasSentImpressionAnalytics) {
            actionListener?.onRecommendationImpression(element)
            element.hasSentImpressionAnalytics = true
        }

        isTopAds = element.recommendationItem.isTopAds
    }

    fun clearImage() {
        binding.productCardView.recycle()
    }
}