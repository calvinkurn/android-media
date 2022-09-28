package com.tokopedia.search.result.product.videowidget

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.video_widget.carousel.VideoCarouselDataView

object VideoCarouselDataViewMapper {

    internal fun InspirationCarouselDataView.Option.toVideoCarouselDataModel(): VideoCarouselDataView {
        return VideoCarouselDataView(
            title = title,
            itemList = product.map { it.toCarouselItemModel() },
            url = url,
            applink = applink,
            bannerApplinkUrl = bannerApplinkUrl,
            bannerImageUrl = bannerImageUrl,
            bannerLinkUrl = bannerLinkUrl,
            identifier = identifier,
            inspirationCarouselType = inspirationCarouselType,
            layout =layout,
            position = position,
            carouselTitle = carouselTitle,
            optionPosition = optionPosition,
            isChipsActive = isChipsActive,
            hexColor = hexColor,
            chipImageUrl = chipImageUrl,
            componentId = componentId,
            trackingOption = trackingOption,
            dimension90 = dimension90,
        )
    }

    internal fun InspirationCarouselDataView.Option.Product.toCarouselItemModel(): VideoCarouselDataView.VideoItem {
        return VideoCarouselDataView.VideoItem(
            id = id,
            videoUrl = customVideoURL,
            imageUrl = imgUrl,
            title = name,
            subTitle = shopName,
            applink = applink,
            name = name,
            price = price,
            priceStr = priceStr,
            rating = rating,
            ratingAverage = ratingAverage,
            countReview = countReview,
            url = url,
            description = description,
            optionPosition = optionPosition,
            inspirationCarouselTitle = inspirationCarouselTitle,
            inspirationCarouselType = inspirationCarouselType,
            layout = layout,
            originalPrice = originalPrice,
            discountPercentage = discountPercentage,
            position = position,
            optionTitle = optionTitle,
            shopLocation = shopLocation,
            shopName = shopName,
            isOrganicAds = isOrganicAds,
            topAdsViewUrl = topAdsViewUrl,
            topAdsClickUrl = topAdsClickUrl,
            topAdsWishlistUrl = topAdsWishlistUrl,
            componentId = componentId,
            dimension90 = dimension90,
        )
    }

    internal fun VideoCarouselDataView.toOptionModel() : InspirationCarouselDataView.Option {
        return InspirationCarouselDataView.Option(
            title = title,
            url = url,
            applink = applink,
            bannerImageUrl = bannerImageUrl,
            bannerLinkUrl = bannerLinkUrl,
            bannerApplinkUrl = bannerApplinkUrl,
            identifier = identifier,
            product = itemList.map { it.toProductModel() },
            inspirationCarouselType = inspirationCarouselType,
            layout = layout,
            position = position,
            carouselTitle = carouselTitle,
            optionPosition = optionPosition,
            isChipsActive = isChipsActive,
            hexColor = hexColor,
            chipImageUrl =chipImageUrl,
            componentId = componentId,
            trackingOption = trackingOption,
            dimension90 = dimension90,
        )
    }

    internal fun VideoCarouselDataView.VideoItem.toProductModel() : InspirationCarouselDataView.Option.Product {
        return  InspirationCarouselDataView.Option.Product(
            id = id,
            customVideoURL = videoUrl,
            imgUrl = imageUrl,
            name = name,
            applink = applink,
            price = price,
            priceStr = priceStr,
            rating = rating,
            ratingAverage = ratingAverage,
            countReview = countReview,
            url = url,
            description = description,
            optionPosition = optionPosition,
            inspirationCarouselTitle = inspirationCarouselTitle,
            inspirationCarouselType = inspirationCarouselType,
            layout = layout,
            originalPrice = originalPrice,
            discountPercentage = discountPercentage,
            position = position,
            optionTitle = optionTitle,
            shopLocation = shopLocation,
            shopName = shopName,
            isOrganicAds = isOrganicAds,
            topAdsViewUrl = topAdsViewUrl,
            topAdsClickUrl = topAdsClickUrl,
            topAdsWishlistUrl = topAdsWishlistUrl,
            componentId = componentId,
            dimension90 = dimension90,
        )
    }
}