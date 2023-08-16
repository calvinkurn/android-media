package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.home_component.visitable.BestSellerDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import com.tokopedia.productcard.ProductCardModel.ProductListType
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity.RecommendationFilterChip
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unifycomponents.CardUnify2
import javax.inject.Inject

class BestSellerMapper @Inject constructor() {

    fun mapChipProductDataModelList(
        recommendationData: List<RecommendationWidget>,
        recommendationFilterList: List<RecommendationFilterChip>,
        bestSellerDataModel: BestSellerDataModel,
        activatedChip: RecommendationFilterChip,
    ): BestSellerDataModel {
        val recommendationWidget = recommendationData.first().copy(
            recommendationFilterChips = recommendationFilterList
        )

        val productList = mapBestSellerProductList(recommendationWidget.recommendationItemList)
        val currentChipProductList = bestSellerDataModel.chipProductList

        val chipProductList = if (currentChipProductList.isEmpty()) {
            recommendationFilterList.map { chip ->
                val isActivated = chip == activatedChip
                mapBestSellerChipProduct(
                    recommendationWidget,
                    chip,
                    isActivated,
                    if (isActivated) productList else listOf(),
                )
            }
        } else {
            currentChipProductList.map {
                if (it.title == activatedChip.title)
                    it.copy(chip = it.chip.activate(), productModelList = productList)
                else
                    it.copy(chip = it.chip.deactivate())
            }
        }

        return bestSellerDataModel.copy(
            chipProductList = chipProductList,
            channelModel = bestSellerDataModel.channelModel.updateHeader(
                defaultTitle = recommendationWidget.title,
                defaultSubtitle = recommendationWidget.subtitle,
            )
        )
    }

    private fun mapBestSellerChipProduct(
        recommendationWidget: RecommendationWidget,
        recommendationFilterChip: RecommendationFilterChip,
        isActivated: Boolean,
        productList: List<BestSellerProductDataModel>,
    ): BestSellerChipProductDataModel =
        BestSellerChipProductDataModel(
            chip = mapBestSellerChip(recommendationFilterChip, isActivated),
            productModelList = productList,
            seeMoreApplink = recommendationWidget.seeMoreAppLink,
        )

    private fun mapBestSellerChip(
        recommendationFilterChip: RecommendationFilterChip,
        isActivated: Boolean,
    ) = BestSellerChipDataModel(
        title = recommendationFilterChip.title,
        value = recommendationFilterChip.value,
        isActivated = isActivated,
        ncpRank = recommendationFilterChip.ncpRank,
        position = recommendationFilterChip.position,
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
                animateOnPress = CardUnify2.ANIMATE_OVERLAY,
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
            header = recommendationItem.header,
            warehouseId = recommendationItem.warehouseId.toString()
        )
}
