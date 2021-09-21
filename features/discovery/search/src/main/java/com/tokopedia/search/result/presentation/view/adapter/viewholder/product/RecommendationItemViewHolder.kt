package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView

class RecommendationItemViewHolder (
        itemView: View,
        val listener: RecommendationListener
) : AbstractViewHolder<RecommendationItemDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_recommendation_card_small_grid
    }

    private val productCardView: ProductCardGridView? by lazy{
        itemView.findViewById<ProductCardGridView>(R.id.productCardView)
    }

    override fun bind(recommendationItemDataView: RecommendationItemDataView) {
        val view = productCardView ?: return
        val recommendationItem = recommendationItemDataView.recommendationItem
        view.setProductModel(recommendationItem.toProductCardModel(hasThreeDots = true))

        view.setOnClickListener {
            listener.onProductClick(recommendationItem, "", adapterPosition)
        }

        view.setImageProductViewHintListener(recommendationItemDataView, createImageProductViewHintListener(recommendationItemDataView))

        view.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(recommendationItemDataView.recommendationItem, adapterPosition)
        }
    }

    private fun createImageProductViewHintListener(recommendationItemDataView: RecommendationItemDataView): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                listener.onProductImpression(recommendationItemDataView.recommendationItem)
            }
        }
    }

    override fun bind(recommendationItemDataView: RecommendationItemDataView, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return

        productCardView?.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(recommendationItemDataView.recommendationItem, adapterPosition)
        }
    }
}