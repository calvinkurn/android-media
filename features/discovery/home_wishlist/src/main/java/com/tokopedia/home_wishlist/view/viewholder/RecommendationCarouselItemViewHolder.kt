package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.utils.ImpresionTask

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Recommendation Carousel Item
 */
class RecommendationCarouselItemViewHolder(
        private val view: View
) : SmartAbstractViewHolder<RecommendationCarouselItemDataModel>(view){

    private val productCardView: ProductCardGridView by lazy { view.findViewById<ProductCardGridView>(R.id.product_item) }

    override fun bind(element: RecommendationCarouselItemDataModel, listener: SmartListener) {
        productCardView.apply {
            setProductModel(
                    element.recommendationItem.toProductCardModel()
            )

            setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener{
                override fun onViewHint() {
                    if(element.recommendationItem.isTopAds){
                        ImpresionTask(className).execute(element.recommendationItem.trackerImageUrl)
                    }
                    (listener as WishlistListener).onProductImpression(element, adapterPosition)
                }
            })

            setOnClickListener {
                (listener as WishlistListener).onProductClick(element, element.parentPosition, adapterPosition)
                if (element.recommendationItem.isTopAds) {
                    ImpresionTask(className).execute(element.recommendationItem.clickUrl)
                }
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel_item
        private const val className: String = "com.tokopedia.home_wishlist.view.viewholder.RecommendationCarouselItemViewHolder"
    }

}