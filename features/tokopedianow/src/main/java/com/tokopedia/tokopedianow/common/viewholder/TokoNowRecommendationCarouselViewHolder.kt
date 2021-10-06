package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetBindPageNameListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel

class TokoNowRecommendationCarouselViewHolder(
    itemView: View,
    private val recommendationCarouselListener: TokoNowRecommendationCarouselListener? = null,
    private val recommendationCarouselWidgetBindPageNameListener: TokonowRecomBindPageNameListener? = null
) : AbstractViewHolder<TokoNowRecommendationCarouselUiModel>(itemView),
    RecommendationCarouselWidgetBindPageNameListener, RecommendationCarouselWidgetListener {

    companion object {
        @LayoutRes
        @JvmStatic
        val LAYOUT = R.layout.item_tokopedianow_recom_carousel
        const val TEXT_OTHER_RECOM = "Rekomendasi Lainnya"
    }

    private val recommendationCarouselWidgetView =
        itemView.findViewById<RecommendationCarouselWidgetView?>(
                    R.id.tokoNowSearchCategoryRecomCarousel
            )

    private var uiModel: TokoNowRecommendationCarouselUiModel? = null

    override fun bind(element: TokoNowRecommendationCarouselUiModel?) {
        uiModel = element ?: return
        val recomWidget = recommendationCarouselWidgetView ?: return
        val scrollToPosition =
            recommendationCarouselListener?.onGetCarouselScrollPosition(adapterPosition)
        if (element.isBindWithPageName) {
            recommendationCarouselWidgetBindPageNameListener?.setViewToLifecycleOwner(recomWidget)
            recomWidget.bindRecomCategoryIds(
                pageName = element.pageName,
                categoryIds = element.categoryId,
                widgetBindPageNameListener = this,
                adapterPosition = adapterPosition,
                scrollToPosition = scrollToPosition.orZero(),
                tempHeaderName = TEXT_OTHER_RECOM,
                isForceRefresh = element.isFirstLoad,
                isTokonow = true
            )
            element.isFirstLoad = false
        } else {
            recomWidget.bindRecomWithData(
                carouselData = element.carouselData,
                adapterPosition = adapterPosition,
                widgetListener = this,
                scrollToPosition = scrollToPosition.orZero(),
            )
            recommendationCarouselListener?.onBindRecommendationCarousel(element, adapterPosition)
        }
    }

    override fun onRecomProductCardImpressed(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int,
    ) {
        recommendationCarouselListener?.onImpressedRecommendationCarouselItem(
                model = uiModel,
                data = data,
                recomItem = recomItem,
                itemPosition = itemPosition,
                adapterPosition = adapterPosition,
        )
    }

    override fun onRecomProductCardClicked(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            applink: String,
            itemPosition: Int,
            adapterPosition: Int,
    ) {
        recommendationCarouselListener?.onClickRecommendationCarouselItem(
            model = uiModel,
            data = data,
            recomItem = recomItem,
            itemPosition = itemPosition,
            adapterPosition = adapterPosition,
        )
    }

    override fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        recommendationCarouselWidgetBindPageNameListener?.onMiniCartUpdatedFromRecomWidget(
            miniCartSimplifiedData
        )
    }

    override fun onRecomTokonowAtcSuccess(message: String) {
        recommendationCarouselWidgetBindPageNameListener?.onRecomTokonowAtcSuccess(message)
    }

    override fun onRecomTokonowAtcFailed(throwable: Throwable) {
        recommendationCarouselWidgetBindPageNameListener?.onRecomTokonowAtcFailed(throwable)
    }

    override fun onRecomTokonowAtcNeedToSendTracker(recommendationItem: RecommendationItem) {
        recommendationCarouselWidgetBindPageNameListener?.onRecomTokonowAtcNeedToSendTracker(
            recommendationItem
        )
    }

    override fun onRecomTokonowDeleteNeedToSendTracker(recommendationItem: RecommendationItem) {
        recommendationCarouselWidgetBindPageNameListener?.onRecomTokonowDeleteNeedToSendTracker(
            recommendationItem
        )
    }

    override fun onClickItemNonLoginState() {
        recommendationCarouselWidgetBindPageNameListener?.onClickItemNonLoginState()
    }

    override fun onRecomProductCardAddToCartNonVariant(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int,
        quantity: Int,
    ) {
        saveCarouselScrollPosition()

        val recommendationCarouselPosition = this.adapterPosition

        recommendationCarouselListener?.onATCNonVariantRecommendationCarouselItem(
                model = uiModel,
                data = data,
                recomItem = recomItem,
                recommendationCarouselPosition = recommendationCarouselPosition,
                quantity = quantity,
        )
    }

    private fun saveCarouselScrollPosition() {
        val adapterPosition = this.adapterPosition
        val carouselScrollPosition =
                recommendationCarouselWidgetView?.getCurrentPosition() ?: 0

        recommendationCarouselListener?.onSaveCarouselScrollPosition(
                adapterPosition = adapterPosition,
                scrollPosition = carouselScrollPosition,
        )
    }

    override fun onRecomProductCardAddVariantClick(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            adapterPosition: Int,
    ) {
        saveCarouselScrollPosition()

        recommendationCarouselListener?.onAddVariantRecommendationCarouselItem(
                model = uiModel,
                data = data,
                recomItem = recomItem,
        )
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {

    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
        recommendationCarouselListener?.onSeeMoreClick(data, applink)
    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) {

    }

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) {

    }

    override fun onRecomBannerClicked(
        data: RecommendationCarouselData,
        applink: String,
        adapterPosition: Int
    ) {

    }

    override fun onChannelWidgetEmpty() {
        recommendationCarouselWidgetView.gone()
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
        recommendationCarouselWidgetView.gone()
    }

    override fun onViewRecycled() {
        saveCarouselScrollPosition()
        super.onViewRecycled()
    }

    interface TokoNowRecommendationCarouselListener {

        fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int)

        fun onGetCarouselScrollPosition(adapterPosition: Int): Int

        fun onBindRecommendationCarousel(
            model: TokoNowRecommendationCarouselUiModel,
            adapterPosition: Int,
        )

        fun onImpressedRecommendationCarouselItem(
            model: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int,
        )

        fun onClickRecommendationCarouselItem(
            model: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
        )

        fun onATCNonVariantRecommendationCarouselItem(
            model: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            recommendationCarouselPosition: Int,
            quantity: Int,
        )

        fun onAddVariantRecommendationCarouselItem(
            model: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
        )

        fun onSeeMoreClick(
            data: RecommendationCarouselData,
            applink: String,
        )

    }

    interface TokonowRecomBindPageNameListener {

        fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData)

        fun onRecomTokonowAtcSuccess(message: String)

        fun onRecomTokonowAtcFailed(throwable: Throwable)

        fun onRecomTokonowAtcNeedToSendTracker(recommendationItem: RecommendationItem)

        fun onRecomTokonowDeleteNeedToSendTracker(recommendationItem: RecommendationItem)

        fun onClickItemNonLoginState()

        //lifecycle owner
        fun setViewToLifecycleOwner(observer: LifecycleObserver)
    }

}