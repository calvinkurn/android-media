package com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogRealtimeClickModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowOverModel
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.data.ThankYouProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.ProductCardViewListener

class ProductCardViewHolder(val view: View) : RecyclerView.ViewHolder(view),
    IAdsViewHolderTrackListener {

    private val productCardView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)


    private lateinit var thankYouProductCardModel: ThankYouProductCardModel

    private var visibleViewPercentage: Int = 0

    fun bind(thankYouProductCardModel: ThankYouProductCardModel,
             listener: ProductCardViewListener?) {
        this.thankYouProductCardModel = thankYouProductCardModel
        thankYouProductCardModel.productCardModel.let { productCardModel ->
            itemView.tag = thankYouProductCardModel
            productCardView.run {
                applyCarousel()
                setProductModel(productCardModel)
                setImageProductViewHintListener(thankYouProductCardModel
                        .recommendationItem, object : ViewHintListener {
                    override fun onViewHint() {
                        listener?.onProductImpression(thankYouProductCardModel
                                .recommendationItem, adapterPosition)
                    }
                })
                setOnClickListener(object : ProductCardClickListener {
                    override fun onClick(v: View) {
                        listener?.onProductClick(thankYouProductCardModel
                            .recommendationItem, null, adapterPosition)
                    }

                    override fun onAreaClicked(v: View) {
                        AppLogTopAds.sendEventRealtimeClick(v.context,
                            thankYouProductCardModel.recommendationItem
                                .asAdsLogRealtimeClickModel(AdsLogConst.Refer.AREA))
                    }

                    override fun onProductImageClicked(v: View) {
                        AppLogTopAds.sendEventRealtimeClick(v.context,
                            thankYouProductCardModel.recommendationItem
                                .asAdsLogRealtimeClickModel(AdsLogConst.Refer.COVER))
                    }

                    override fun onSellerInfoClicked(v: View) {
                        AppLogTopAds.sendEventRealtimeClick(v.context,
                            thankYouProductCardModel.recommendationItem
                                .asAdsLogRealtimeClickModel(AdsLogConst.Refer.SELLER_NAME))
                    }
                })

                setThreeDotsOnClickListener {
                    listener?.onThreeDotsAllProductClicked(thankYouProductCardModel)
                }
            }
        }

    }

    fun getThankYouRecommendationModel(): ThankYouProductCardModel? {
        if (::thankYouProductCardModel.isInitialized)
            return thankYouProductCardModel
        return null
    }


    companion object {
        val LAYOUT_ID = R.layout.thank_item_recommendation
    }

    override fun setVisiblePercentage(visiblePercentage: Int) {
        this.visibleViewPercentage = visiblePercentage
    }

    override val visiblePercentage: Int
        get() = visibleViewPercentage

    override fun onViewAttachedToWindow() {
        if (thankYouProductCardModel.recommendationItem.isTopAds){
            AppLogTopAds.sendEventShow(view.context,
                thankYouProductCardModel.recommendationItem.asAdsLogShowModel())
        }
    }

    override fun onViewDetachedFromWindow(visiblePercentage: Int) {
        if (thankYouProductCardModel.recommendationItem.isTopAds){
            AppLogTopAds.sendEventShowOver(view.context,
                thankYouProductCardModel.recommendationItem.asAdsLogShowOverModel(visiblePercentage))
        }
    }
}
