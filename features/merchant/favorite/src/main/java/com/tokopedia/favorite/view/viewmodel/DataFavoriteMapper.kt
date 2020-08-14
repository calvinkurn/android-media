package com.tokopedia.favorite.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.FavoriteShopItem
import com.tokopedia.favorite.domain.model.TopAdsShop
import java.util.*
import javax.inject.Inject

/**
 * @author madi on 4/6/17.
 */
class DataFavoriteMapper @Inject constructor() {

    fun prepareDataFavoriteShop(favoriteShop: FavoriteShopItem): FavoriteShopViewModel {
        val favoriteShopViewModel = FavoriteShopViewModel()
        favoriteShopViewModel.shopId = favoriteShop.id
        favoriteShopViewModel.shopName = favoriteShop.name
        favoriteShopViewModel.shopAvatarImageUrl = favoriteShop.iconUri
        favoriteShopViewModel.shopLocation = favoriteShop.location
        favoriteShopViewModel.isFavoriteShop = favoriteShop.isFav
        favoriteShopViewModel.badgeUrl = favoriteShop.badgeUrl
        return favoriteShopViewModel
    }

    fun prepareDataTopAdsShop(adsShop: TopAdsShop): TopAdsShopViewModel {
        val shopViewModel = TopAdsShopViewModel()
        val shopItems = ArrayList<TopAdsShopItem>()
        val topAdsShopItemList = adsShop.topAdsShopItemList
        if (topAdsShopItemList != null) {
            for (item in topAdsShopItemList) {
                val shopItem = TopAdsShopItem()
                shopItem.shopId = item.shopId
                shopItem.shopDomain = item.shopDomain
                shopItem.shopName = item.shopName
                shopItem.adKey = item.adRefKey
                shopItem.shopClickUrl = item.shopClickUrl
                shopItem.shopCoverUrl = item.shopImageCover
                shopItem.shopCoverEcs = item.shopImageCoverEcs
                shopItem.shopImageUrl = item.shopImageUrl
                shopItem.shopImageEcs = item.shopImageEcs
                shopItem.shopLocation = item.shopLocation
                shopItem.isFav = item.isSelected
                shopItems.add(shopItem)
            }
        }
        shopViewModel.adsShopItems = shopItems
        return shopViewModel
    }

    fun prepareListFavoriteShop(favoriteShop: FavoriteShop): List<Visitable<*>> {
        val elementList: MutableList<Visitable<*>> = ArrayList()
        val data = favoriteShop.data
        if (data != null) {
            for (favoriteShopItem in data) {
                favoriteShopItem.isFav = (true)
                elementList.add(prepareDataFavoriteShop(favoriteShopItem))
            }
        }
        return elementList
    }
}
