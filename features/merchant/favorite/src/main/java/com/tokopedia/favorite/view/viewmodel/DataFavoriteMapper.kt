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

    fun prepareDataTopAdsShop(topAdsShop: TopAdsShop): TopAdsShopViewModel {
        val topAdsShopViewModel = TopAdsShopViewModel()
        val viewModelTopAdsShopItemList = ArrayList<TopAdsShopItem>()
        val topAdsShopItemList = topAdsShop.topAdsShopItemList

        if (topAdsShopItemList != null) {
            for (topAdsShopItem in topAdsShopItemList) {
                val shopItem = TopAdsShopItem()
                shopItem.shopId = topAdsShopItem.shopId
                shopItem.shopDomain = topAdsShopItem.shopDomain
                shopItem.shopName = topAdsShopItem.shopName
                shopItem.adKey = topAdsShopItem.adRefKey
                shopItem.shopClickUrl = topAdsShopItem.shopClickUrl
                shopItem.shopCoverUrl = topAdsShopItem.shopImageCover
                shopItem.shopCoverEcs = topAdsShopItem.shopImageCoverEcs
                shopItem.shopImageUrl = topAdsShopItem.shopImageUrl
                shopItem.shopImageEcs = topAdsShopItem.shopImageEcs
                shopItem.shopLocation = topAdsShopItem.shopLocation
                shopItem.isFav = topAdsShopItem.isSelected
                viewModelTopAdsShopItemList.add(shopItem)
            }
        }

        topAdsShopViewModel.adsShopItems = viewModelTopAdsShopItemList
        return topAdsShopViewModel
    }

    fun prepareListFavoriteShop(favoriteShop: FavoriteShop): List<Visitable<*>> {
        val elementList: MutableList<Visitable<*>> = ArrayList()
        val data = favoriteShop.data
        if (data != null) {
            for (favoriteShopItem in data) {
                favoriteShopItem.isFav = true
                elementList.add(prepareDataFavoriteShop(favoriteShopItem))
            }
        }
        return elementList
    }
}
