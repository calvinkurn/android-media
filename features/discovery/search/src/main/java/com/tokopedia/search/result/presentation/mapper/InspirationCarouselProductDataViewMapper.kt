package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.search.result.domain.model.SearchProductModel.*
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import java.util.*

class InspirationCarouselProductDataViewMapper {

    fun convertToInspirationCarouselProductDataView(
            inspirationCarouselProduct: List<InspirationCarouselProduct>,
            productPosition: Int,
            inspirationCarouselType: String,
            layout: String,
            mapLabelGroupDataViewList: (List<ProductLabelGroup>) -> List<LabelGroupDataView>,
            optionTitle: String,

            ): List<InspirationCarouselDataView.Option.Product> {

        return inspirationCarouselProduct.mapIndexed { index, product ->
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
                    convertInspirationCarouselProductBadgeToBadgesItemList(product.badgeList),
            )
        }
    }

    private fun convertInspirationCarouselProductBadgeToBadgesItemList(
            badgesList: List<InspirationCarouselProductBadge>,
    ): List<BadgeItemDataView> {
        val badgeItemList: MutableList<BadgeItemDataView> = ArrayList()
        for (badgeModel in badgesList) {
            badgeItemList.add(convertInspirationCarouselProductBadgeToBadgeItem(badgeModel))
        }
        return badgeItemList
    }

    private fun convertInspirationCarouselProductBadgeToBadgeItem(
            badgeModel: InspirationCarouselProductBadge,
    ): BadgeItemDataView {
        val badgeItem = BadgeItemDataView()
        badgeItem.title = badgeModel.title
        badgeItem.imageUrl = badgeModel.imageUrl
        badgeItem.isShown = badgeModel.isShown
        return badgeItem
    }
}