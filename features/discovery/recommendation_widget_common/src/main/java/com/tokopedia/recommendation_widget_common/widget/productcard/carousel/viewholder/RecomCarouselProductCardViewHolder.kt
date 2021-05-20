package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
        setLayout(itemView.context, element)
    }

    private fun setLayout(context: Context, element: RecomCarouselProductCardDataModel){
        productCardView?.run{
            applyCarousel()
            setProductModel(element.productModel)
            addOnImpressionListener(element.impressHolder) {
//                if(element.recomItem.isTopAds){
//                    TopAdsUrlHitter(className).hitImpressionUrl(context, element.recomItem.impression,
//                            element.recomItem.productId,
//                            element.recomItem.name,
//                            element.recomItem.imageUrl,
//                            element.componentName)
//                }
                element.listener?.onProductCardImpressed(position = adapterPosition,data = data, recomItem = element.recomItem)
            }
            setOnClickListener {
//                if(element.recomItem.isTopAds){
//                    TopAdsUrlHitter(className).hitClickUrl(context, element.recomItem.productClickUrl,
//                            element.recomItem.productId,
//                            element.recomItem.name,
//                            element.recomItem.imageUrl,
//                            element.componentName)
//                }
                element.listener?.onProductCardClicked(position = adapterPosition,data = data, recomItem = element.recomItem, applink = element.recomItem.appUrl)
            }
        }
    }
}