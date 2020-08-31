package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationDataModel
import com.tokopedia.product.detail.view.adapter.AddToCartRecommendationProductAdapter
import com.tokopedia.product.detail.view.adapter.RecommendationProductTypeFactory
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import kotlinx.android.synthetic.main.add_to_cart_done_recommendation_layout.view.*

class AddToCartDoneRecommendationViewHolder(
        itemView: View,
        private val recommendationListener: RecommendationListener
) : AbstractViewHolder<AddToCartDoneRecommendationDataModel>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_recommendation_layout
    }

    private val adapter: AddToCartRecommendationProductAdapter by lazy {
        AddToCartRecommendationProductAdapter(fact)
    }

    private val fact: RecommendationProductTypeFactory by lazy {
        RecommendationProductTypeFactory(recommendationListener)
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
                    productCardModelList = products.map {
                        ProductCardModel(
                                slashedPrice = it.slashedPrice,
                                productName = it.name,
                                formattedPrice = it.price,
                                productImageUrl = it.imageUrl,
                                isTopAds = it.isTopAds,
                                discountPercentage = it.discountPercentage.toString(),
                                reviewCount = it.countReview,
                                ratingCount = it.rating,
                                shopLocation = it.location,
                                isWishlistVisible = true,
                                isWishlisted = it.isWishlist,
                                shopBadgeList = it.badgesUrl.map {
                                    ProductCardModel.ShopBadge(imageUrl = it
                                            ?: "")
                                },
                                freeOngkir = ProductCardModel.FreeOngkir(
                                        isActive = it.isFreeOngkirActive,
                                        imageUrl = it.freeOngkirImageUrl
                                ),
                                labelGroupList = it.labelGroupList.map { recommendationLabel ->
                                    ProductCardModel.LabelGroup(
                                            position = recommendationLabel.position,
                                            title = recommendationLabel.title,
                                            type = recommendationLabel.type
                                    )
                                }
                        )
                    }

            )
            visible()
        }
    }
}