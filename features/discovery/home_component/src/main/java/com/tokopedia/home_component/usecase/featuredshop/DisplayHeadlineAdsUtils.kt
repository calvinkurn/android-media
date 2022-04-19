package com.tokopedia.home_component.usecase.featuredshop

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelShop

/**
 * Created by yfsx on 05/08/21.
 */
fun List<DisplayHeadlineAdsEntity.DisplayHeadlineAds>.mappingTopAdsHeaderToChannelGrid(): List<ChannelGrid>{
    return this.map {
        ChannelGrid(
                id = it.id,
                applink = it.applink,
                shop = ChannelShop(
                        id = it.headline.shop.id,
                        shopName = it.headline.shop.name,
                        shopProfileUrl = it.headline.shop.imageShop.cover,
                        shopLocation = it.headline.shop.location,
                        shopBadgeUrl = it.headline.badges.firstOrNull()?.imageUrl ?: "",
                        isGoldMerchant = it.headline.shop.goldShop,
                        isOfficialStore = it.headline.shop.shopIsOfficialStore
                ),
                countReviewFormat = it.headline.shop.products.firstOrNull()?.review ?: "",
                rating = it.headline.shop.products.firstOrNull()?.rating ?: 0,
                impression = it.headline.image.url,
                productClickUrl = it.adClickUrl,
                imageUrl = it.headline.shop.products.firstOrNull()?.imageProduct?.imageUrl ?: ""
        )
    }
}