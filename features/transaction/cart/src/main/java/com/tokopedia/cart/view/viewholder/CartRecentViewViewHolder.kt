package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecentViewBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
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

        private const val RECOM_WIDGET_TOP_PADDING_DP = 20
        private const val RECOM_WIDGET_BOTTOM_PADDING_DP = 20
    }

    fun bind(element: CartRecentViewHolderData) {
        binding.recommendationWidgetView.bind(
            model = RecommendationWidgetModel(
                metadata = element.recommendationWidgetMetadata,
                listener = object : RecommendationWidgetListener {

                    override fun onViewAttachedToWindow(
                        position: Int,
                        item: RecommendationItem
                    ) {
                        item.sendShowAdsByteIo(itemView.context)
                    }

                    override fun onViewDetachedFromWindow(
                        position: Int,
                        item: RecommendationItem,
                        visiblePercentage: Int
                    ) {
                        item.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
                    }

                    override fun onProductClick(
                        position: Int,
                        item: RecommendationItem
                    ): Boolean {
                        listener?.onRecentViewProductClicked(position, item)
                        return true
                    }

                    override fun onAreaClicked(position: Int, item: RecommendationItem) {
                        item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                    }

                    override fun onProductImageClicked(
                        position: Int,
                        item: RecommendationItem
                    ) {
                        item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                    }

                    override fun onSellerInfoClicked(
                        position: Int,
                        item: RecommendationItem
                    ) {
                        item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
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
                        listener?.onAddToCartRecentViewClicked(item)
                        return false
                    }

                    override fun onProductAddToCartSuccess(
                        item: RecommendationItem,
                        addToCartData: AddToCartDataModel
                    ): Boolean {
                        listener?.onAddToCartRecentViewSuccess(item, addToCartData)
                        return true
                    }

                    override fun onProductAddToCartFailed(): Boolean {
                        listener?.onAddToCartRecentViewFailed()
                        return true
                    }
                }
            ),
            callback = object : RecommendationWidgetView.Callback {
                override fun onShow(visitableList: List<RecommendationVisitable>?) {
                    super.onShow(visitableList)
                    val recommendationModel = visitableList?.find { visitable ->
                        visitable is RecommendationCarouselModel
                    } as? RecommendationCarouselModel
                    val recommendationItems: List<RecommendationItem> =
                        recommendationModel?.widget?.recommendationItemList ?: emptyList()
                    if (!element.hasSentImpressionAnalytics && recommendationItems.isNotEmpty()) {
                        listener?.onRecentViewImpression(recommendationItems)
                        element.hasSentImpressionAnalytics = true
                    }
                    with(binding) {
                        recommendationWidgetView.setPadding(
                            0,
                            root.context.pxToDp(RECOM_WIDGET_TOP_PADDING_DP).toInt(),
                            0,
                            root.context.pxToDp(RECOM_WIDGET_BOTTOM_PADDING_DP).toInt()
                        )
                        recommendationWidgetView.visible()
                    }
                }

                override fun onError() {
                    super.onError()
                    with(binding) {
                        recommendationWidgetView.setPadding(0, 0, 0, 0)
                        recommendationWidgetView.gone()
                    }
                }
            }
        )
    }

    fun recycle() {
        binding.recommendationWidgetView.recycle()
    }
}
