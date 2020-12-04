package com.tokopedia.recommendation_widget_common.widget.bestseller.mapper

import android.content.Context
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import kotlinx.coroutines.Dispatchers

/**
 * Created by Lukas on 05/11/20.
 */
class BestSellerMapper (
        private val context: Context
){
    suspend fun mappingRecommendationWidget(recommendationWidget: RecommendationWidget): BestSellerDataModel{
        val productList = mappingProductCards(recommendationWidget.recommendationItemList)
        return BestSellerDataModel(
                title = recommendationWidget.title,
                height = getMaxHeightProductCards(productList),
                pageName = recommendationWidget.pageName,
                productCardModelList = productList,
                recommendationItemList = recommendationWidget.recommendationItemList,
                filterChip = recommendationWidget.recommendationFilterChips,
                seeMoreAppLink = recommendationWidget.seeMoreAppLink
        )
    }

    private fun mappingProductCards(recommendationList: List<RecommendationItem>): List<ProductCardModel> {
        return recommendationList.map { recommendationItem ->
            ProductCardModel(
                    slashedPrice = recommendationItem.slashedPrice,
                    productName = recommendationItem.name,
                    formattedPrice = recommendationItem.price,
                    productImageUrl = recommendationItem.imageUrl,
                    isTopAds = recommendationItem.isTopAds,
                    discountPercentage = recommendationItem.discountPercentage,
                    reviewCount = recommendationItem.countReview,
                    ratingCount = recommendationItem.rating,
                    shopLocation = recommendationItem.location,
                    isWishlistVisible = false,
                    isWishlisted = recommendationItem.isWishlist,
                    shopBadgeList = recommendationItem.badgesUrl.map {
                        ProductCardModel.ShopBadge(imageUrl = it
                                ?: "")
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = recommendationItem.isFreeOngkirActive,
                            imageUrl = recommendationItem.freeOngkirImageUrl
                    ),
                    labelGroupList = recommendationItem.labelGroupList.map { recommendationLabel ->
                        ProductCardModel.LabelGroup(
                                position = recommendationLabel.position,
                                title = recommendationLabel.title,
                                type = recommendationLabel.type
                        )
                    }
            )

        }
    }

    suspend fun getMaxHeightProductCards(productCardModels: List<ProductCardModel>): Int{
        return productCardModels.getMaxHeightForGridView(
                context = context,
                coroutineDispatcher = Dispatchers.IO,
                productImageWidth = context.resources.getDimensionPixelSize(R.dimen.product_card_carousel_item_width)
        )
    }
}