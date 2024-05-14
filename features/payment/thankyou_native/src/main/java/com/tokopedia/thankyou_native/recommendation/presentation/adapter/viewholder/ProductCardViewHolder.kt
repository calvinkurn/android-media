package com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.data.ThankYouProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.ProductCardViewListener

class ProductCardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val productCardView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

    private lateinit var thankYouProductCardModel: ThankYouProductCardModel

    fun bind(
        thankYouProductCardModel: ThankYouProductCardModel,
        listener: ProductCardViewListener?
    ) {
        this.thankYouProductCardModel = thankYouProductCardModel
        thankYouProductCardModel.productCardModel.let { productCardModel ->
            itemView.tag = thankYouProductCardModel
            productCardView.run {
                applyCarousel()
                setProductModel(productCardModel)
                setImageProductViewHintListener(thankYouProductCardModel
                    .recommendationItem, object : ViewHintListener {
                    override fun onViewHint() {
                        listener?.onProductImpression(
                            thankYouProductCardModel
                                .recommendationItem, adapterPosition
                        )
                    }
                })
                setOnClickListener(object : ProductCardClickListener {
                    override fun onClick(v: View) {
                        listener?.onProductClick(
                            thankYouProductCardModel
                                .recommendationItem, null, adapterPosition
                        )
                    }

                    override fun onAreaClicked(v: View) {
                        if (::thankYouProductCardModel.isInitialized)
                            thankYouProductCardModel.recommendationItem.sendRealtimeClickAdsByteIo(
                                v.context, AdsLogConst.Refer.AREA
                            )
                    }

                    override fun onProductImageClicked(v: View) {
                        if (::thankYouProductCardModel.isInitialized)
                            thankYouProductCardModel.recommendationItem.sendRealtimeClickAdsByteIo(
                                v.context, AdsLogConst.Refer.COVER
                            )
                    }

                    override fun onSellerInfoClicked(v: View) {
                        if (::thankYouProductCardModel.isInitialized)
                            thankYouProductCardModel.recommendationItem.sendRealtimeClickAdsByteIo(
                                v.context, AdsLogConst.Refer.SELLER_NAME
                            )
                    }
                })

                setThreeDotsOnClickListener {
                    listener?.onThreeDotsAllProductClicked(thankYouProductCardModel)
                }

                setVisibilityPercentListener(
                    isTopAds = thankYouProductCardModel.recommendationItem.isTopAds,
                    eventListener = object : ProductConstraintLayout.OnVisibilityPercentChanged {
                        override fun onShow() {
                            thankYouProductCardModel.recommendationItem.sendShowAdsByteIo(itemView.context)
                        }

                        override fun onShowOver(maxPercentage: Int) {
                            thankYouProductCardModel.recommendationItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
                        }
                    }
                )
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
}
