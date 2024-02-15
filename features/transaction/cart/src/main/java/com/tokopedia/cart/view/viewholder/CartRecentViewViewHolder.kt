package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecentViewBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.CartViewModel
import com.tokopedia.cart.view.adapter.recentview.CartRecentViewAdapter
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetView

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewViewHolder(
    private val binding: ItemCartRecentViewBinding,
    val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    var recentViewAdapter: CartRecentViewAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_cart_recent_view
    }

    fun bind(element: CartRecentViewHolderData) {
        binding.recommendationWidgetView.bind(
            model = RecommendationWidgetModel(
                metadata = element.recommendationWidgetMetadata,
                source = RecommendationWidgetSource.Cart(
                    userId = "123" // TODO: wiring
                ),
                listener = object : RecommendationWidgetListener {
                    override fun onProductClick(item: RecommendationItem): Boolean {
                        listener?.onRecentViewProductClicked(item)
                        return super.onProductClick(item)
                    }
                }
            ),
            callback = object : RecommendationWidgetView.Callback {
                override fun onShow() {
                    super.onShow()
                    binding.recommendationWidgetView.visible()
                }

                override fun onError() {
                    super.onError()
                    binding.recommendationWidgetView.gone()
                }
            }
        )
//        if (recentViewAdapter == null) {
//            recentViewAdapter = CartRecentViewAdapter(listener)
//        }
//        recentViewAdapter?.recentViewItemHoldeDataList = element.recentViewList
//        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
//        binding.rvRecentView.layoutManager = layoutManager
//        binding.rvRecentView.adapter = recentViewAdapter
//        val itemDecorationCount = binding.rvRecentView.itemDecorationCount
//        if (itemDecorationCount > 0) {
//            binding.rvRecentView.removeItemDecorationAt(0)
//        }
//        val padding = itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
//        binding.rvRecentView.addItemDecoration(CartHorizontalItemDecoration(padding, padding))
//        binding.rvRecentView.scrollToPosition(element.lastFocussPosition)
//        if (!element.hasSentImpressionAnalytics) {
//            listener?.onRecentViewImpression()
//            element.hasSentImpressionAnalytics = true
//        }
    }

    fun recycle() {
        binding.recommendationWidgetView.recycle()
    }
}
