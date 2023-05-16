package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationProductItemBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationProductViewHolder(
    itemView: View,
    private val recommendationListener: RecommendationListener
): BaseViewHolder(itemView) {

    private val binding: UniversalInboxRecommendationProductItemBinding? by viewBinding()

    fun bind(uiModel: RecommendationItem) {
        binding?.inboxProductRecommendation?.run {
            setProductModel(uiModel.toProductCardModel(hasThreeDots = true))
            setImageProductViewHintListener(uiModel, object: ViewHintListener {
                override fun onViewHint() {
                    recommendationListener.onProductImpression(uiModel)
                }
            })

            setOnClickListener {
                recommendationListener.onProductClick(uiModel, null, layoutPosition)
            }

            setThreeDotsOnClickListener {
                recommendationListener.onThreeDotsClick(uiModel, layoutPosition)
            }
        }
    }

    fun bind(uiModel: RecommendationItem, payloads: MutableList<Any>) {
        val isWishlisted = payloads.getOrNull(0) as? Boolean ?: return

        uiModel.isWishlist = isWishlisted

        binding?.inboxProductRecommendation?.setThreeDotsOnClickListener {
            recommendationListener.onThreeDotsClick(uiModel, layoutPosition)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_product_item
    }
}
