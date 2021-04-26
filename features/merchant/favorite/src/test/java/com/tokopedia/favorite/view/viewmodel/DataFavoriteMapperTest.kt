package com.tokopedia.favorite.view.viewmodel

import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.FavoriteShopItem
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.favorite.dummyFavoriteShopItemList
import com.tokopedia.favorite.dummyTopAdsShopItemList
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DataFavoriteMapperTest {

    @Test
    fun `prepareDataFavoriteShop should return view model with correct data`() {
        val item = FavoriteShopItem(
                id = "id",
                name = "Name",
                iconUri = "iconUri",
                location = "location",
                isFav = true,
                badgeUrl = "badgeUrl"
        )
        val viewModel = DataFavoriteMapper.prepareDataFavoriteShop(item)
        assertEquals(item.id, viewModel.shopId)
        assertEquals(item.name, viewModel.shopName)
        assertEquals(item.iconUri, viewModel.shopAvatarImageUrl)
        assertEquals(item.location, viewModel.shopLocation)
        assertEquals(item.isFav, viewModel.isFavoriteShop)
        assertEquals(item.badgeUrl, viewModel.badgeUrl)
    }

    @Test
    fun `prepareDataTopAdsShop with topAdsShop with null topAdsShopItemList should return topAdsShowViewModel with empty topAdsShopItem list`() {
        val topAdsShop = TopAdsShop(topAdsShopItemList = null)
        val viewModel = DataFavoriteMapper.prepareDataTopAdsShop(topAdsShop)
        assertNotNull(viewModel.adsShopItems)
        assertTrue(viewModel.adsShopItems!!.isEmpty())
    }

    @Test
    fun `prepareDataTopAdsShop with topAdsShop should return topAdsShowViewModel with correct topAdsShopItem list`() {
        val numOfItems = 2
        val topAdsShop = TopAdsShop(topAdsShopItemList = dummyTopAdsShopItemList(numOfItems))
        val viewModel = DataFavoriteMapper.prepareDataTopAdsShop(topAdsShop)

        assertNotNull(viewModel.adsShopItems)
        assertTrue(viewModel.adsShopItems!!.size == numOfItems)

        var i = 0
        for (item in viewModel.adsShopItems!!) {
            val shopItem = topAdsShop.topAdsShopItemList!![i]
            assertEquals(shopItem.shopId, item.shopId)
            assertEquals(shopItem.shopDomain, item.shopDomain)
            assertEquals(shopItem.shopName, item.shopName)
            assertEquals(shopItem.adRefKey, item.adKey)
            assertEquals(shopItem.shopClickUrl, item.shopClickUrl)
            assertEquals(shopItem.shopImageCover, item.shopCoverUrl)
            assertEquals(shopItem.shopImageCoverEcs, item.shopCoverEcs)
            assertEquals(shopItem.shopImageUrl, item.shopImageUrl)
            assertEquals(shopItem.shopImageEcs, item.shopImageEcs)
            assertEquals(shopItem.shopLocation, item.shopLocation)
            assertEquals(shopItem.isSelected, item.isFav)
            i++
        }
    }

    @Test
    fun `prepareListFavoriteShop with favoriteShop's data is null should return empty list of visitables`() {
        val favoriteShop = FavoriteShop()
        val visitables = DataFavoriteMapper.prepareListFavoriteShop(favoriteShop)
        assertTrue(visitables.isEmpty())
    }

    @Test
    fun `prepareListFavoriteShop should return list of visitables with isFav is true`() {
        val numOfItems = 10
        val favoriteShop = FavoriteShop(data = dummyFavoriteShopItemList(numOfItems))
        val favoriteShopList = DataFavoriteMapper.prepareListFavoriteShop(favoriteShop)

        assertTrue(favoriteShopList.size == numOfItems)

        for (i in 0 until numOfItems) {
            assertTrue((favoriteShopList[i] as FavoriteShopUiModel).isFavoriteShop)
        }
    }

}
