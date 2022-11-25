package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.applink.RouteManager
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

class ProductRecommendationOocCallback(
    private val lifecycle: Lifecycle,
    private val trackingQueue: TrackingQueue?,
    private val eventLabel: String,
    private val activity: FragmentActivity?,

    /**
     * Analytics Purpose
     */
    private val eventActionClicked: String,
    private val eventActionImpressed: String,
    private val eventCategory: String,
    private val getListValue: (RecommendationItem) -> String,
    private val userSession: UserSessionInterface
) : TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener,
    TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener {

    override fun setViewToLifecycleOwner(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }

    override fun onImpressedRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        trackingQueue?.putEETracking(
            ProductRecommendationTracking.getImpressionProductTracking(
                recommendationItem = recomItem,
                eventCategory = eventCategory,
                headerTitle = data.recommendationData.title,
                position = itemPosition,
                isLoggedIn = userSession.isLoggedIn,
                userId = userSession.userId,
                eventLabel = eventLabel,
                eventAction = eventActionImpressed,
                listValue = getListValue(recomItem),
            )
        )
    }

    override fun onClickRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            ProductRecommendationTracking.getClickProductTracking(
                recommendationItem = recomItem,
                eventCategory = eventCategory,
                headerTitle = data.recommendationData.title,
                position = itemPosition,
                isLoggedIn = userSession.isLoggedIn,
                userId = userSession.userId,
                eventLabel = eventLabel,
                eventAction = eventActionClicked,
                listValue = getListValue(recomItem),
            )
        )
        RouteManager.route(activity, recomItem.appUrl)
    }

    override fun onSeeMoreClick(data: RecommendationCarouselData, applink: String) {
        RouteManager.route(activity, applink)
    }

    override fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData) { /* nothing to do */ }

    override fun onRecomTokonowAtcSuccess(message: String) { /* nothing to do */ }

    override fun onRecomTokonowAtcFailed(throwable: Throwable) { /* nothing to do */ }

    override fun onRecomTokonowAtcNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) { /* nothing to do */ }

    override fun onRecomTokonowDeleteNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) { /* nothing to do */ }

    override fun onClickItemNonLoginState() { /* nothing to do */ }

    override fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int) { /* nothing to do */ }

    override fun onGetCarouselScrollPosition(adapterPosition: Int): Int = 0

    override fun onBindRecommendationCarousel(
        model: TokoNowProductRecommendationOocUiModel,
        adapterPosition: Int
    ) { /* nothing to do */ }

    override fun onATCNonVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        recommendationCarouselPosition: Int,
        quantity: Int
    ) { /* nothing to do */ }

    override fun onAddVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem
    ) { /* nothing to do */ }

}
