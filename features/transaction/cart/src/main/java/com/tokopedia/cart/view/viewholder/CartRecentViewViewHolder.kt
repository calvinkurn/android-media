package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecentViewBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetView

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewViewHolder(
    private val binding: ItemCartRecentViewBinding,
    val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_recent_view
    }

    fun bind(element: CartRecentViewHolderData) {
        binding.recommendationWidgetView.bind(
            model = RecommendationWidgetModel(
                metadata = element.recommendationWidgetMetadata,
                listener = object : RecommendationWidgetListener {

                    override fun onProductClick(
                        position: Int,
                        item: RecommendationItem
                    ): Boolean {
                        listener?.onRecentViewProductClicked(position, item)
                        return true
                    }

                    override fun onProductImpress(
                        position: Int,
                        item: RecommendationItem
                    ): Boolean {
                        listener?.onRecentViewProductImpression(position, item)
                        return true
                    }

                    override fun onProductAddToCartClick(
                        item: RecommendationItem
                    ): Boolean {
                        listener?.onButtonAddToCartClicked(item)
                        return true
                    }
                }
            ),
            callback = object : RecommendationWidgetView.Callback {
                override fun onShow() {
                    super.onShow()
                    // TODO: Adjust recommendation data for impression tracking
                    if (element.hasSentImpressionAnalytics) {
                        listener?.onRecentViewImpression(emptyList())
                        element.hasSentImpressionAnalytics = true
                    }
                    binding.recommendationWidgetView.visible()
                }

                override fun onError() {
                    super.onError()
                    binding.recommendationWidgetView.gone()
                }
            }
        )
    }

    fun recycle() {
        binding.recommendationWidgetView.recycle()
    }
}
