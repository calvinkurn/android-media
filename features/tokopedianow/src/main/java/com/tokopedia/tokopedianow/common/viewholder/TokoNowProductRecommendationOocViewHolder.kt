package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowPageNameListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecomCarouselBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowProductRecommendationOocViewHolder(
    itemView: View,
    private val recommendationCarouselListener: TokoNowRecommendationCarouselListener? = null,
    private val recommendationCarouselWidgetBindPageNameListener: TokonowRecomBindPageNameListener? = null
) : AbstractViewHolder<TokoNowProductRecommendationOocUiModel>(itemView),
    RecomCarouselWidgetBasicListener, RecommendationCarouselTokonowListener,
    RecommendationCarouselTokonowPageNameListener {

    companion object {
        @LayoutRes
        @JvmStatic
        val LAYOUT = R.layout.item_tokopedianow_recom_carousel
    }

    private var binding: ItemTokopedianowRecomCarouselBinding? by viewBinding()

    private var uiModel: TokoNowProductRecommendationOocUiModel? = null

    override fun bind(element: TokoNowProductRecommendationOocUiModel?) {
        uiModel = element ?: return
        val scrollToPosition =
            recommendationCarouselListener?.onGetCarouselScrollPosition(adapterPosition)
        binding?.tokoNowSearchCategoryRecomCarousel?.show()
        if (element.isBindWithPageName) {
            binding?.tokoNowSearchCategoryRecomCarousel?.let {
                recommendationCarouselWidgetBindPageNameListener?.setViewToLifecycleOwner(it)
                it.bind(
                    pageName = element.pageName,
                    tokonowPageNameListener = this,
                    basicListener = this,
                    adapterPosition = adapterPosition,
                    scrollToPosition = scrollToPosition.orZero(),
                    isForceRefresh = element.isFirstLoad,
                    isTokonow = element.isTokoNow,
                    categoryIds = element.categoryId,
                    keyword = element.keywords,
                    miniCartSource = element.miniCartSource
                )
                element.isFirstLoad = false
            }
        } else {
            if(element.carouselData.recommendationData.recommendationItemList.isNotEmpty()) {
                binding?.tokoNowSearchCategoryRecomCarousel?.bind(
                    carouselData = element.carouselData,
                    adapterPosition = adapterPosition,
                    basicListener = this,
                    tokonowListener = this,
                    scrollToPosition = scrollToPosition.orZero()
                )
            }
            recommendationCarouselListener?.onBindRecommendationCarousel(
                element,
                adapterPosition
            )
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

    override fun onRecomTokonowAtcNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) {
        recommendationCarouselWidgetBindPageNameListener?.onRecomTokonowAtcNeedToSendTracker(
            recommendationItem
        )
    }

    override fun onRecomTokonowDeleteNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) {
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
            binding?.tokoNowSearchCategoryRecomCarousel?.getCurrentPosition() ?: 0

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
        binding?.tokoNowSearchCategoryRecomCarousel?.gone()
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
        binding?.tokoNowSearchCategoryRecomCarousel?.gone()
    }

    override fun onShowError(pageName: String, e: Throwable) {
    }

    override fun onViewRecycled() {
        saveCarouselScrollPosition()
        super.onViewRecycled()
    }

    interface TokoNowRecommendationCarouselListener {

        fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int)

        fun onGetCarouselScrollPosition(adapterPosition: Int): Int

        fun onBindRecommendationCarousel(
            model: TokoNowProductRecommendationOocUiModel,
            adapterPosition: Int,
        )

        fun onImpressedRecommendationCarouselItem(
            model: TokoNowProductRecommendationOocUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int,
        )

        fun onClickRecommendationCarouselItem(
            model: TokoNowProductRecommendationOocUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
        )

        fun onATCNonVariantRecommendationCarouselItem(
            model: TokoNowProductRecommendationOocUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            recommendationCarouselPosition: Int,
            quantity: Int,
        )

        fun onAddVariantRecommendationCarouselItem(
            model: TokoNowProductRecommendationOocUiModel?,
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

        fun onRecomTokonowAtcNeedToSendTracker(
            recommendationItem: RecommendationItem
        )

        fun onRecomTokonowDeleteNeedToSendTracker(
            recommendationItem: RecommendationItem
        )

        fun onClickItemNonLoginState()

        //lifecycle owner
        fun setViewToLifecycleOwner(observer: LifecycleObserver)
    }

}
