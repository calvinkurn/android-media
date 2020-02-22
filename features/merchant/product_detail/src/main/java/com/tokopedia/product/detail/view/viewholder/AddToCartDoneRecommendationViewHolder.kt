package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationDataModel
import com.tokopedia.product.detail.view.adapter.AddToCartRecommendationProductAdapter
import com.tokopedia.product.detail.view.adapter.RecommendationProductTypeFactory
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.unifycomponents.Toaster
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
                        override fun onItemClick(productCardModel: ProductCardModel, adapterPosition: Int) {
                            val productRecommendation = products[adapterPosition]
                            recommendationListener.onProductClick(
                                    productRecommendation,
                                    null,
                                    parentPosition,
                                    adapterPosition
                            )
                        }
                    },
                    carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                        override fun getImpressHolder(adapterPosition: Int): ImpressHolder {
                            return products[adapterPosition]
                        }

                        override fun onItemImpressed(productCardModel: ProductCardModel, adapterPosition: Int) {
                            val productRecommendation = products[adapterPosition]
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
                                    ProductCardModel.ShopBadge(imageUrl = it?:"")
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

    fun updateWishlist(position: Int, isAddWishlist: Boolean) {
        with(itemView) {
            product_recom.updateWishlist(position, isAddWishlist)
        }
    }

}