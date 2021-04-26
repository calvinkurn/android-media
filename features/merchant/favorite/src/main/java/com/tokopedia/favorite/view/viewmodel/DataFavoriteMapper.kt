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
object DataFavoriteMapper {

    fun prepareDataFavoriteShop(favoriteShop: FavoriteShopItem): FavoriteShopUiModel {
        val favoriteShopUiModel = FavoriteShopUiModel()
        favoriteShopUiModel.shopId = favoriteShop.id
        favoriteShopUiModel.shopName = favoriteShop.name
        favoriteShopUiModel.shopAvatarImageUrl = favoriteShop.iconUri
        favoriteShopUiModel.shopLocation = favoriteShop.location
        favoriteShopUiModel.isFavoriteShop = favoriteShop.isFav
        favoriteShopUiModel.badgeUrl = favoriteShop.badgeUrl
        return favoriteShopUiModel
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
}
