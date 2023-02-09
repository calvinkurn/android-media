package com.tokopedia.tokopedianow.search.domain.mapper

import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

object SearchBroadMatchMapper {
    fun createBroadMatchDataView(
        otherRelated: AceSearchProductModel.OtherRelated,
        cartService: CartService
    ) = BroadMatchDataView(
            seeMoreModel = TokoNowSeeMoreCardCarouselUiModel(
                  appLink = otherRelated.applink
            ),
            headerModel = TokoNowDynamicHeaderUiModel(
                title = otherRelated.keyword,
                ctaTextLink = otherRelated.applink
            ),
            broadMatchItemModelList = otherRelated.productList
                .map { otherRelatedProduct ->
                    TokoNowProductCardCarouselItemUiModel(
                        appLink = otherRelatedProduct.applink,
                        productCardModel = TokoNowProductCardViewUiModel (
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
                            usePreDraw = true
                        ),
                        alternativeKeyword = otherRelated.keyword,
                        shopId = otherRelatedProduct.shop.id
                    )
                }
            )

}
