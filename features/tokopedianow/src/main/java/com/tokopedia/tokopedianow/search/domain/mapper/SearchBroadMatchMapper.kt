package com.tokopedia.tokopedianow.search.domain.mapper

import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel.LabelGroup
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

object SearchBroadMatchMapper {
    fun createBroadMatchDataView(
        otherRelated: AceSearchProductModel.OtherRelated,
        cartService: CartService,
        hasBlockedAddToCart: Boolean
    ) = BroadMatchDataView(
            seeMoreModel = ProductCardCompactCarouselSeeMoreUiModel(
                  appLink = otherRelated.applink
            ),
            headerModel = TokoNowDynamicHeaderUiModel(
                title = otherRelated.keyword,
                ctaTextLink = otherRelated.applink
            ),
            broadMatchItemModelList = otherRelated.productList
                .map { otherRelatedProduct ->
                    ProductCardCompactCarouselItemUiModel(
                        appLink = otherRelatedProduct.applink,
                        productCardModel = ProductCardCompactUiModel (
                            productId = otherRelatedProduct.id,
                            name = otherRelatedProduct.name,
                            price = otherRelatedProduct.priceString,
                            imageUrl = otherRelatedProduct.imageUrl,
                            rating = otherRelatedProduct.ratingAverage,
                            labelGroupList = otherRelatedProduct.labelGroupList.map {
                                LabelGroup(
                                    it.position,
                                    it.title,
                                    it.type,
                                    it.url
                                )
                            },
                            minOrder = otherRelatedProduct.minOrder,
                            maxOrder = otherRelatedProduct.maxOrder,
                            availableStock = otherRelatedProduct.stock,
                            orderQuantity = cartService.getProductQuantity(otherRelatedProduct.id),
                            needToShowQuantityEditor = true,
                            hasBlockedAddToCart = hasBlockedAddToCart,
                            usePreDraw = true
                        ),
                        alternativeKeyword = otherRelated.keyword,
                        shopId = otherRelatedProduct.shop.id
                    )
                }
            )

}
