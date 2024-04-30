package com.tokopedia.recommendation_widget_common.infinite.main

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewModelDelegate
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext

class InfiniteRecommendationManager(
    private val context: Context,
    val listener: InfiniteRecommendationListener? = null,
    private val headingType: Int = 0,
    private val additionalAppLogParam: AppLogAdditionalParam = AppLogAdditionalParam.None,
) : InfiniteRecommendationCallback {

    val adapter: InfiniteRecommendationAdapter by getAdapter()
    private val viewModel: InfiniteRecommendationViewModel? by getViewModel()

    var requestParam: GetRecommendationRequestParam = GetRecommendationRequestParam()
        set(value) {
            field = value.copy(
                hasNewProductCardEnabled = true
            )
            viewModel?.init(additionalAppLogParam)
        }

    init {
        observeRecommendationProducts()
    }

    private fun observeRecommendationProducts() {
        val lifecycleOwner = context as? LifecycleOwner ?: return
        viewModel?.components?.observe(lifecycleOwner) { components ->
            adapter.submitList(components)
        }
    }

    private fun getAdapter() = lazy {
        InfiniteRecommendationAdapter(this, headingType)
    }

    private fun getViewModel() = InfiniteRecommendationViewModelDelegate {
        context.getActivityFromContext()
    }

    override fun fetchRecommendation() {
        viewModel?.fetchComponents(requestParam)
    }

    override fun onImpressProductCard(
        recommendationItem: RecommendationItem,
        additionalAppLogParam: AppLogAdditionalParam
    ) {
        AppLogRecommendation.sendProductShowAppLog(
            recommendationItem.asProductTrackModel(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                additionalParam = additionalAppLogParam
            )
        )
        listener?.onImpressProductCard(recommendationItem)
    }

    override fun onClickViewAll(recommendationWidget: RecommendationWidget) {
        listener?.onClickViewAll(recommendationWidget)
    }

    override fun onClickProductCard(
        recommendationItem: RecommendationItem,
        additionalAppLogParam: AppLogAdditionalParam
    ) {
        AppLogRecommendation.sendProductClickAppLog(
            recommendationItem.asProductTrackModel(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                additionalParam = additionalAppLogParam
            )
        )
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            recommendationItem.productId.toString()
        )
        listener?.onClickProductCard(recommendationItem)
    }
}
