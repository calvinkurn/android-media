package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.productcard.ProductCardModel

object ProductCardColumnListMapper {
    private const val PERCENTAGE_CHAR = '%'

    fun List<ComponentsItem>.mapToCarouselPagingGroupProductModel(): CarouselPagingGroupProductModel {
        return CarouselPagingGroupProductModel(
            group = CarouselPagingGroupModel(),
            productItemList = mapToProductCardModel()
        )
    }

    private fun List<ComponentsItem>.mapToProductCardModel(): List<ProductCardModel>  {
        return map {
            it.data?.firstOrNull()?.let { item ->
                ProductCardModel(
                    productName = item.name.toString(),
                    productImageUrl = item.imageUrlMobile.orEmpty(),
                    formattedPrice = item.price.orEmpty(),
                    slashedPrice = item.discountedPrice.orEmpty(),
                    discountPercentage = if (item.discountPercentage?.lastOrNull() != PERCENTAGE_CHAR && item.discountPercentage?.lastOrNull() != null) "${item.discountPercentage}${PERCENTAGE_CHAR}" else item.discountPercentage.orEmpty(),
                    countSoldRating = item.averageRating,
                    labelGroupList = item.labelsGroupList?.map { label ->
                        ProductCardModel.LabelGroup(
                            position = label.position,
                            title = label.title,
                            type = label.type,
                            imageUrl = label.url
                        )
                    }.orEmpty(),
                    freeOngkir = ProductCardModel.FreeOngkir(
                        isActive = item.freeOngkir?.isActive.orFalse(),
                        imageUrl = item.freeOngkir?.freeOngkirImageUrl.orEmpty()
                    ),
                    productListType = ProductCardModel.ProductListType.BEST_SELLER
                )
            } ?: ProductCardModel()
        }
    }
}
