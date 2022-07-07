package com.tokopedia.favorite.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.FavoriteShopItem
import com.tokopedia.favorite.domain.model.TopAdsShop
import java.util.*

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

    fun prepareDataTopAdsShop(topAdsShop: TopAdsShop): TopAdsShopUiModel {
        val topAdsShopViewModel = TopAdsShopUiModel()
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
                shopItem.imageUrl = topAdsShopItem.imageUrl
                shopItem.fullEcs = topAdsShopItem.fullEcs
                shopItem.shopIsOfficial = topAdsShopItem.shopIsOfficial
                shopItem.isPMPro = topAdsShopItem.isPMPro
                shopItem.isPowerMerchant = topAdsShopItem.isPowerMerchant
                shopItem.imageShop = topAdsShopItem.imageShop
                shopItem.layout = topAdsShopItem.layout
                viewModelTopAdsShopItemList.add(shopItem)
            }
        }

        topAdsShopViewModel.adsShopItems = viewModelTopAdsShopItemList
        return topAdsShopViewModel
    }
}
