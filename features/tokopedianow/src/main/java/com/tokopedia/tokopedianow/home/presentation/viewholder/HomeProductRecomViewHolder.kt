package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeProductRecomBinding
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeProductRecomViewHolder(
    itemView: View,
    private val tokoNowView: TokoNowView? = null,
    private val listener: HomeProductRecomListener? = null,
    private val rtrListener: RealTimeRecommendationListener? = null,
    private val rtrAnalytics: RealTimeRecommendationAnalytics? = null
) : AbstractViewHolder<HomeProductRecomUiModel>(itemView),
    RecomCarouselWidgetBasicListener, RecommendationCarouselTokonowListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_product_recom
    }

    private var binding: ItemTokopedianowHomeProductRecomBinding? by viewBinding()

    private var channelId = ""
    private var isOoc = false

    override fun bind(element: HomeProductRecomUiModel) {
        channelId = element.id
        isOoc = element.id == PRODUCT_RECOM_OOC
        renderProductCarousel(element)
        setOnScrollListener()
        restoreScrollState()
        if (isOoc) {
            binding?.divider?.show()
            val spaceZero = itemView.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
            val spaceSixTeen = itemView.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            binding?.carouselProductRecom?.setMargin(spaceZero, spaceSixTeen, spaceZero, spaceZero)
        }
        renderRealTimeRecommendation(element)
    }

    override fun bind(element: HomeProductRecomUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            renderProductCarousel(element = element)
            renderRealTimeRecommendation(element = element)
        }
    }

    private fun renderProductCarousel(element: HomeProductRecomUiModel) {
        binding?.carouselProductRecom?.bind(
            carouselData = RecommendationCarouselData(
                recommendationData = element.recomWidget,
                state = RecommendationCarouselData.STATE_READY,
            ),
            basicListener = this,
            tokonowListener = this
        )
    }

    private fun renderRealTimeRecommendation(element: HomeProductRecomUiModel) {
        binding?.realTimeRecommendationCarousel?.apply {
            listener = rtrListener
            analytics = rtrAnalytics
            bind(element.realTimeRecom)
        }
    }

    override fun onRecomBannerImpressed(
        data: RecommendationCarouselData,
        adapterPosition: Int
    ) { /* nothing to do */
    }

    override fun onRecomBannerClicked(
        data: RecommendationCarouselData,
        applink: String,
        adapterPosition: Int
    ) { /* nothing to do */
    }

    override fun onChannelWidgetEmpty() { /* nothing to do */
    }

    override fun onChannelExpired(
        data: RecommendationCarouselData,
        channelPosition: Int
    ) { /* nothing to do */
    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) { /* nothing to do */
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
        //should remove widget
    }

    override fun onShowError(pageName: String, e: Throwable) {

    }

    override fun onRecomProductCardImpressed(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        listener?.onRecomProductCardImpressed(
            recomItem,
            channelId,
            data.recommendationData.title,
            data.recommendationData.pageName,
            isOoc
        )
    }

    override fun onSeeAllBannerClicked(
        data: RecommendationCarouselData,
        applink: String
    ) {
        listener?.onSeeAllBannerClicked(channelId, data.recommendationData.title, isOoc, applink)
    }

    override fun onRecomProductCardClicked(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        applink: String,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        listener?.onRecomProductCardClicked(recomItem, channelId, data.recommendationData.title, recomItem.position.toString(), isOoc, applink)
    }

    override fun onRecomProductCardAddToCartNonVariant(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int,
        quantity: Int
    ) {
        listener?.onProductRecomNonVariantClick(recomItem, quantity, data.recommendationData.title, channelId, recomItem.position.toString())
    }

    override fun onRecomProductCardAddVariantClick(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int
    ) {
        AtcVariantHelper.goToAtcVariant(
            context = itemView.context,
            productId = recomItem.productId.toString(),
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = recomItem.shopId.toString(),
            startActivitResult = (tokoNowView?.getFragmentPage() as TokoNowHomeFragment)::startActivityForResult
        )
    }

    private fun setOnScrollListener() {
        binding?.carouselProductRecom?.setScrollListener { scrollState ->
            tokoNowView?.saveScrollState(adapterPosition, scrollState)
        }
    }

    private fun restoreScrollState() {
        val scrollState = tokoNowView?.getScrollState(adapterPosition)
        binding?.carouselProductRecom?.restoreScrollState(scrollState)
    }

    interface HomeProductRecomListener {
        fun onRecomProductCardClicked(
            recomItem: RecommendationItem,
            channelId: String,
            headerName: String,
            position: String,
            isOoc: Boolean,
            applink: String
        )

        fun onRecomProductCardImpressed(
            recomItem: RecommendationItem,
            channelId: String,
            headerName: String,
            pageName: String,
            isOoc: Boolean
        )

        fun onSeeAllBannerClicked(
            channelId: String,
            headerName: String,
            isOoc: Boolean,
            applink: String
        )

        fun onProductRecomNonVariantClick(
            recomItem: RecommendationItem,
            quantity: Int,
            headerName: String,
            channelId: String,
            position: String
        )
    }
}
