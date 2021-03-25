package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationDataModel
import com.tokopedia.product.detail.view.adapter.RecommendationProductTypeFactory
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import kotlinx.android.synthetic.main.add_to_cart_done_recommendation_layout.view.*

class AddToCartDoneRecommendationViewHolder(
        itemView: View,
        private val recommendationListener: RecommendationListener
) : AbstractViewHolder<AddToCartDoneRecommendationDataModel>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_recommendation_layout
    }

    override fun bind(element: AddToCartDoneRecommendationDataModel) {
        with(itemView) {
            title_recom.text = element.recommendationWidget.title
            val products = element.recommendationWidget.recommendationItemList
            val parentPosition = adapterPosition
            product_recom.bindCarouselProductCardViewGrid(
                    carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                        override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val productRecommendation = products.getOrNull(carouselProductCardPosition) ?: return
                            recommendationListener.onProductClick(
                                    productRecommendation,
                                    null,
                                    parentPosition,
                                    carouselProductCardPosition
                            )
                        }
                    },
                    carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                        override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                            return products.getOrNull(carouselProductCardPosition)
                        }

                        override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val productRecommendation = products.getOrNull(carouselProductCardPosition) ?: return
                            recommendationListener.onProductImpression(productRecommendation)
                        }
                    },
                    carouselProductCardOnItemThreeDotsClickListener = object : CarouselProductCardListener.OnItemThreeDotsClickListener {
                        override fun onItemThreeDotsClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val product = products.getOrNull(carouselProductCardPosition) ?: return
                            recommendationListener.onThreeDotsClick(product, adapterPosition, carouselProductCardPosition)
                        }
                    },
                    productCardModelList = products.map { it.toProductCardModel(hasThreeDots = true) }

            )
            visible()
        }
    }
}