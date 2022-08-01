package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.model.BroadMatch
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchProduct
import com.tokopedia.search.result.presentation.model.CarouselOptionType
import com.tokopedia.search.result.presentation.model.CarouselProductType
import com.tokopedia.search.result.presentation.model.DynamicCarouselOption
import com.tokopedia.search.result.presentation.model.DynamicCarouselProduct
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.product.separator.VerticalSeparator

object BroadMatchModelMapper {

    fun convertToBroadMatchDataView(
        option: InspirationCarouselDataView.Option,
        type: String,
        addTopSeparator: Boolean,
        index: Int,
        size: Int,
    ): BroadMatchDataView {
        val verticalSeparator = if (index == 0 && addTopSeparator) {
            VerticalSeparator.Top
        } else if (index == size - 1) {
            VerticalSeparator.Bottom
        } else {
            VerticalSeparator.None
        }
        return BroadMatchDataView(
            keyword = option.title,
            subtitle = option.subtitle,
            applink = option.applink,
            carouselOptionType = determineCarouselOptionType(type, option),
            broadMatchItemDataViewList = option.product.mapIndexed { index, product ->
                BroadMatchItemDataView(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    imageUrl = product.imgUrl,
                    url = product.url,
                    applink = product.applink,
                    priceString = product.priceStr,
                    ratingAverage = product.ratingAverage,
                    labelGroupDataList = product.labelGroupDataList,
                    badgeItemDataViewList = product.badgeItemDataViewList,
                    shopLocation = product.shopLocation,
                    shopName = product.shopName,
                    position = index + 1,
                    alternativeKeyword = option.title,
                    carouselProductType = determineInspirationCarouselProductType(
                        type,
                        option,
                        product,
                    ),
                    freeOngkirDataView = product.freeOngkirDataView,
                    isOrganicAds = product.isOrganicAds,
                    topAdsViewUrl = product.topAdsViewUrl,
                    topAdsClickUrl = product.topAdsClickUrl,
                    topAdsWishlistUrl = product.topAdsWishlistUrl,
                    componentId = product.componentId,
                    originalPrice = product.originalPrice,
                    discountPercentage = product.discountPercentage,
                )
            },
            cardButton = BroadMatchDataView.CardButton(
                option.cardButton.title,
                option.cardButton.applink,
            ),
            verticalSeparator = verticalSeparator,
        )
    }

    private fun determineCarouselOptionType(
        type: String,
        option: InspirationCarouselDataView.Option
    ): CarouselOptionType =
        if (type == SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_KEYWORD) BroadMatch
        else DynamicCarouselOption(option)

    private fun determineInspirationCarouselProductType(
        type: String,
        option: InspirationCarouselDataView.Option,
        product: InspirationCarouselDataView.Option.Product,
    ): CarouselProductType {
        return if (type == SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_KEYWORD)
            BroadMatchProduct(false)
        else
            DynamicCarouselProduct(option.inspirationCarouselType, product)
    }
}