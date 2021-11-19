package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationCarouselItemBinding

class WishlistV2RecommendationCarouselViewHolder(private val binding: WishlistV2RecommendationCarouselItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(element: WishlistV2TypeLayoutData) {
            if (element.dataObject is WishlistV2RecommendationDataModel) {
                val data = element.dataObject.recommendationData
                binding.carousel.bindCarouselProductCardViewGrid(
                    productCardModelList = convertIntoProductDataModel(data),
                    carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener{
                        override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            val wishlistDataModel = data.getOrNull(carouselProductCardPosition) ?: return

                        }
                    },
//                    carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener{
//                        override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
//                            return data.getOrNull(carouselProductCardPosition)?.recommendationItemList
//                        }
//
//                        override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
//                            val wishlistDataModel = element.list.getOrNull(carouselProductCardPosition) ?: return
//                            (listener as WishlistListener).onProductImpression(wishlistDataModel, carouselProductCardPosition)
//                        }
//                    }
                )
            }

        }

    private fun convertIntoProductDataModel(data: List<RecommendationItem>): List<ProductCardModel> {
        return data.map { element ->
            ProductCardModel(
                slashedPrice = element.slashedPrice,
                productName = element.name,
                formattedPrice = element.price,
                productImageUrl = element.imageUrl,
                isTopAds = element.isTopAds,
                discountPercentage = element.discountPercentage,
                reviewCount = element.countReview,
                ratingCount = element.rating,
                shopLocation = element.location,
                isWishlistVisible = true,
                isWishlisted = element.isWishlist,
                shopBadgeList = element.badgesUrl.map {
                    ProductCardModel.ShopBadge(imageUrl = it ?: "")
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                    isActive = element.isFreeOngkirActive,
                    imageUrl = element.freeOngkirImageUrl
                ),
                labelGroupList = element.labelGroupList.map { recommendationLabel ->
                    ProductCardModel.LabelGroup(
                        position = recommendationLabel.position,
                        title = recommendationLabel.title,
                        type = recommendationLabel.type,
                        imageUrl = recommendationLabel.imageUrl
                    )
                }
            )
        } ?: listOf()
    }
}