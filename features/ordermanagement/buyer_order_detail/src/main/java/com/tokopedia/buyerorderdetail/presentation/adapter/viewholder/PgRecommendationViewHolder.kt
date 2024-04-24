package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.*

open class PgRecommendationViewHolder(
    itemView: View,
    private val buyerOrderDetailBindRecomWidgetListener: BuyerOrderDetailBindRecomWidgetListener
) : AbstractViewHolder<PGRecommendationWidgetUiModel>(itemView),
    RecomCarouselWidgetBasicListener, RecomCarouselWidgetBasicListener.OnAdsItemClickListener,
    RecomCarouselWidgetBasicListener.OnAdsViewListener {

    companion object {
        val LAYOUT = R.layout.buyer_order_detail_pg_recommendation_layout

        const val DEFAULT_PAGE_NAME = "default"
    }

    private val recomWidget: RecommendationCarouselWidgetView? =
        itemView as? RecommendationCarouselWidgetView

    override fun bind(element: PGRecommendationWidgetUiModel) {
        recomWidget.run {
            if (this == null) {
                buyerOrderDetailBindRecomWidgetListener.hidePgRecommendation()
            } else {
                buyerOrderDetailBindRecomWidgetListener.setViewToLifecycleOwner(this)
                val pageName = element.pageName.ifBlank { DEFAULT_PAGE_NAME }
                this.bind(
                    pageName = pageName,
                    productIds = element.productIdList,
                    adapterPosition = adapterPosition,
                    basicListener = this@PgRecommendationViewHolder,
                    tokonowPageNameListener = null,
                    tempHeaderName = getString(R.string.bom_recommendation_lbl),
                    adsItemClickListener = this@PgRecommendationViewHolder,
                    adsViewListener = this@PgRecommendationViewHolder
                )
            }
        }
    }

    override fun onViewAttachedToWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendShowAdsByteIo(itemView.context)
    }

    override fun onViewDetachedFromWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int, visiblePercentage: Int) {
        recomItem.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
    }

    override fun onAreaClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
    }

    override fun onProductImageClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
    }

    override fun onSellerInfoClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {
    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
        buyerOrderDetailBindRecomWidgetListener.onSeeAllProductCardClick(applink)
    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) {
    }

    override fun onRecomProductCardImpressed(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        buyerOrderDetailBindRecomWidgetListener.onProductCardImpress(recomItem)
    }

    override fun onRecomProductCardClicked(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        applink: String,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        buyerOrderDetailBindRecomWidgetListener.onProductCardClick(recomItem, applink)
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
        buyerOrderDetailBindRecomWidgetListener.hidePgRecommendation()
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
        buyerOrderDetailBindRecomWidgetListener.hidePgRecommendation()
    }

    override fun onShowError(pageName: String, e: Throwable) {
    }

    interface BuyerOrderDetailBindRecomWidgetListener {

        fun onProductCardClick(recommendationItem: RecommendationItem, applink: String)

        fun onProductCardImpress(recommendationItem: RecommendationItem)

        fun onSeeAllProductCardClick(appLink: String)

        fun setViewToLifecycleOwner(observer: LifecycleObserver)

        fun hidePgRecommendation()
    }
}
