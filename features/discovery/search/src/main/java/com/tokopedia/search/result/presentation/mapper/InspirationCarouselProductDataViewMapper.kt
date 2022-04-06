package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProduct
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProductBadge
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProductFreeOngkir
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView

class InspirationCarouselProductDataViewMapper {

    @Suppress("LongParameterList")
    fun convertToInspirationCarouselProductDataView(
        inspirationCarouselProduct: List<InspirationCarouselProduct>,
        productPosition: Int,
        inspirationCarouselType: String,
        layout: String,
        mapLabelGroupDataViewList: (List<ProductLabelGroup>) -> List<LabelGroupDataView>,
        optionTitle: String,
        carouselTitle: String,
        dimension90: String,
    ): List<InspirationCarouselDataView.Option.Product> {

        return inspirationCarouselProduct.mapIndexed { index, product ->
            val isOrganicAds = product.isOrganicAds()

            InspirationCarouselDataView.Option.Product(
                product.id,
                product.name,
                product.price,
                product.priceStr,
                product.imgUrl,
                product.rating,
                product.countReview,
                product.url,
                product.applink,
                product.description,
                productPosition,
                inspirationCarouselType,
                product.ratingAverage,
                mapLabelGroupDataViewList(product.labelGroupList),
                layout,
                product.originalPrice,
                product.discountPercentage,
                index + 1,
                optionTitle,
                product.shop.city,
                product.shop.name,
                convertInspirationCarouselProductBadgeToBadgesItemList(product.badgeList),
                product.freeOngkir.mapToFreeOngkirDataView(),
                isOrganicAds,
                product.ads.productViewUrl,
                product.ads.productClickUrl,
                product.ads.productWishlistUrl,
                product.componentId,
                carouselTitle,
                dimension90,
            )
        }
    }

    private fun convertInspirationCarouselProductBadgeToBadgesItemList(
            badgesList: List<InspirationCarouselProductBadge>,
    ): List<BadgeItemDataView> {
        return badgesList.map { badgeModel ->
            BadgeItemDataView(
                    title = badgeModel.title,
                    imageUrl = badgeModel.imageUrl,
                    isShown = badgeModel.isShown,
            )
        }
    }

    private fun InspirationCarouselProductFreeOngkir.mapToFreeOngkirDataView() =
        FreeOngkirDataView(isActive, imageUrl)
}