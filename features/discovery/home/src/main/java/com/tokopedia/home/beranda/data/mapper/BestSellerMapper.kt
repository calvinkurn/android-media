package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.ProductListType
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity.RecommendationFilterChip
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

class BestSellerMapper @Inject constructor() {

    fun mapBestSellerChipProduct(
        recommendationWidget: RecommendationWidget,
        recommendationFilterChip: RecommendationFilterChip,
        productList: List<BestSellerProductDataModel>,
    ): BestSellerChipProductDataModel =
        BestSellerChipProductDataModel(
            chip = mapBestSellerChip(recommendationWidget, recommendationFilterChip),
            productModelList = productList
        )

    private fun mapBestSellerChip(
        recommendationWidget: RecommendationWidget,
        recommendationFilterChip: RecommendationFilterChip,
    ) = BestSellerChipDataModel(
        title = recommendationFilterChip.title,
        value = recommendationFilterChip.value,
        isActivated = recommendationFilterChip.isActivated,
        seeMoreApplink = recommendationWidget.seeMoreAppLink,
    )

    fun mapBestSellerProductList(
        recommendationItemList: List<RecommendationItem>
    ): List<BestSellerProductDataModel> =
        recommendationItemList.map(::mapBestSellerProduct)

    private fun mapBestSellerProduct(
        recommendationItem: RecommendationItem
    ): BestSellerProductDataModel =
        BestSellerProductDataModel(
            productCardModel = recommendationItem.toProductCardModel(
                productCardListType = ProductListType.BEST_SELLER,
            ),
            applink = recommendationItem.appUrl,
            productId = recommendationItem.productId.toString(),
            name = recommendationItem.name,
            isTopAds = recommendationItem.isTopAds,
            recommendationType = recommendationItem.recommendationType,
            price = recommendationItem.priceInt.toLong(),
            position = recommendationItem.position,
            isFreeOngkirActive = recommendationItem.isFreeOngkirActive,
            cartId = recommendationItem.cartId,
            categoryBreadcrumbs = recommendationItem.categoryBreadcrumbs,
            pageName = recommendationItem.pageName,
            header = recommendationItem.header
        )
}
