package com.tokopedia.product.detail.postatc.view.component.recommendation

import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.product.detail.databinding.ItemGlobalRecommendationBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMiniCart
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource

class GlobalRecommendationViewHolder(
    private val binding: ItemGlobalRecommendationBinding,
    private val callback: PostAtcCallback
) : PostAtcViewHolder<RecommendationUiModel>(binding.root) {

    override fun bind(element: RecommendationUiModel) {
        binding.apply {
            postAtcGlobalRecom.bind(
                model = recommendationWidgetModel(element),
                parentRootView = callback.rootView()
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
                queryParam = element.queryParam
            ),
            miniCart = RecommendationWidgetMiniCart(
                miniCartSource = MiniCartSource.PDP
            ),
            source = RecommendationWidgetSource.PDPAfterATC(
                anchorProductId = element.productId,
                isUserLoggedIn = callback.userSession.isLoggedIn,
                userId = callback.userSession.userId,
                warehouseId = element.warehouseId
            )
        )
}
