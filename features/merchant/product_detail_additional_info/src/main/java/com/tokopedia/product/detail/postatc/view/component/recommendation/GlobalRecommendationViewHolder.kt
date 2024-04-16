package com.tokopedia.product.detail.postatc.view.component.recommendation

import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.product.detail.databinding.ItemGlobalRecommendationBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMiniCart
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetView

class GlobalRecommendationViewHolder(
    private val binding: ItemGlobalRecommendationBinding,
    private val callback: PostAtcCallback
) : PostAtcViewHolder<RecommendationUiModel>(binding.root) {

    override fun bind(element: RecommendationUiModel) {
        binding.apply {
            postAtcGlobalRecom.bind(
                model = recommendationWidgetModel(element),
                parentRootView = callback.rootView(),
                callback = recommendationWidgetCallback(element)
            )

            root.addOnImpressionListener(element.impressHolder) {
                callback.impressComponent(getComponentTrackData(element))
            }
        }
    }

    private fun recommendationWidgetModel(element: RecommendationUiModel) =
        RecommendationWidgetModel(
            metadata = RecommendationWidgetMetadata(
                pageName = element.name,
                productIds = listOf(element.productId),
                queryParam = element.queryParam,
                criteriaThematicIDs = listOf(element.thematicId)
            ),
            miniCart = RecommendationWidgetMiniCart(
                miniCartSource = MiniCartSource.PDP
            ),
            source = RecommendationWidgetSource.PDPAfterATC(
                anchorProductId = element.productId,
                isUserLoggedIn = callback.userSession.isLoggedIn,
                userId = callback.userSession.userId,
                warehouseId = element.warehouseId,
                offerId = element.offerId,
                shopId = element.shopId
            ),
            listener = object : RecommendationWidgetListener {
                override fun onProductClick(position: Int, item: RecommendationItem): Boolean {
                    callback.dismiss()
                    return false
                }

                override fun onAreaClicked(position: Int, item: RecommendationItem) {
                    item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(position: Int, item: RecommendationItem) {
                    item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(position: Int, item: RecommendationItem) {
                    item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
                }

                override fun onViewAttachedToWindow(position: Int, item: RecommendationItem) {
                    item.sendShowAdsByteIo(itemView.context)
                }

                override fun onViewDetachedFromWindow(position: Int, item: RecommendationItem, visiblePercentage: Int) {
                    item.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
                }
            }
        )

    private fun recommendationWidgetCallback(element: RecommendationUiModel) =
        object : RecommendationWidgetView.Callback {
            override fun onError() {
                callback.removeComponent(element.id)
            }
        }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding.postAtcGlobalRecom.recycle()
    }
}
