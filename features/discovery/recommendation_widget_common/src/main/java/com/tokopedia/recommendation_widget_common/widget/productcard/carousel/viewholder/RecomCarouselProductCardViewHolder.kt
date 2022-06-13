package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselProductCardDataModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

/**
 * Created by yfsx on 5/3/21.
 */
class RecomCarouselProductCardViewHolder (view: View,
                                          val data: RecommendationWidget):
        AbstractViewHolder<RecomCarouselProductCardDataModel>(view) {

    companion object{
        val LAYOUT = R.layout.item_recom_carousel_productcard
        private const val className = "com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder.RecomCarouselProductCardViewHolder"
    }

    private val productCardView: ProductCardGridView? by lazy { view.findViewById<ProductCardGridView>(R.id.productCardView) }
    override fun bind(element: RecomCarouselProductCardDataModel) {
        setLayout(element)
        setupListener(itemView.context, element)
    }

    override fun bind(element: RecomCarouselProductCardDataModel, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull().takeIf { it is Map<*, *> } as? Map<*, *>
        if (payload.isNullOrEmpty()) {
            bind(element)
        } else {
            if (payload.containsKey(RecomCarouselProductCardDataModel.PAYLOAD_FLAG_SHOULD_UPDATE_PRODUCT_CARD)) {
                setLayout(element)
            }
            if (payload.containsKey(RecomCarouselProductCardDataModel.PAYLOAD_FLAG_SHOULD_UPDATE_LISTENERS)) {
                setupListener(itemView.context, element)
            }
        }
    }

    private fun setLayout(element: RecomCarouselProductCardDataModel){
        productCardView?.run{
            applyCarousel()
            setProductModel(element.productModel)
        }
    }

    private fun setupListener(context: Context, element: RecomCarouselProductCardDataModel) {
        productCardView?.run {
            addOnImpressionListener(element.recomItem) {
                if(element.recomItem.isTopAds){
                    TopAdsUrlHitter(context).hitImpressionUrl(
                            className,
                            element.recomItem.trackerImageUrl,
                            element.recomItem.productId.toString(),
                            element.recomItem.name,
                            element.recomItem.imageUrl,
                            element.componentName)
                }
                element.listener?.onProductCardImpressed(position = adapterPosition,data = data, recomItem = element.recomItem)
            }
            setOnClickListener {
                if(element.recomItem.isTopAds){
                    TopAdsUrlHitter(context).hitClickUrl(
                            className,
                            element.recomItem.clickUrl,
                            element.recomItem.productId.toString(),
                            element.recomItem.name,
                            element.recomItem.imageUrl,
                            element.componentName)
                }
                element.listener?.onProductCardClicked(position = adapterPosition,data = data, recomItem = element.recomItem, applink = element.recomItem.appUrl)
            }
            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    element.recomItem.onCardQuantityChanged(quantity)
                    element.listener?.onRecomProductCardAddToCartNonVariant(
                            data = data,
                            recomItem = element.recomItem,
                            adapterPosition = adapterPosition,
                            quantity = quantity,
                    )
                }
            })
            setAddVariantClickListener {
                element.listener?.onRecomProductCardAddVariantClick(
                        data = data,
                        recomItem = element.recomItem,
                        adapterPosition = adapterPosition,
                )
            }
        }
    }

    override fun onViewRecycled() {
        productCardView?.recycle()
    }
}