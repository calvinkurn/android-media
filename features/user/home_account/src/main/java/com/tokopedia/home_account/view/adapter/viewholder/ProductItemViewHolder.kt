package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountRecommendationItemProductCardBinding
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class ProductItemViewHolder(itemView: View, val listener: HomeAccountUserListener) :
    BaseViewHolder(itemView) {

    private val binding: HomeAccountRecommendationItemProductCardBinding? by viewBinding()

    fun bind(element: RecommendationItem) {
        binding?.productCardView?.setProductModel(element.toProductCardModel(hasThreeDots = true))
        binding?.productCardView?.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductRecommendationImpression(element, adapterPosition)
                }
            })

        binding?.productCardView?.setOnClickListener {
            listener.onProductRecommendationClicked(element, adapterPosition)
        }

        binding?.productCardView?.setThreeDotsOnClickListener {
            listener.onProductRecommendationThreeDotsClicked(element, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_recommendation_item_product_card
    }
}