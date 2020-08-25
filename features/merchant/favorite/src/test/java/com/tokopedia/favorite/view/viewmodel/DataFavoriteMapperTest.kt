package com.tokopedia.favorite.view.viewmodel

import com.tokopedia.favorite.domain.model.*
import com.tokopedia.favorite.domain.model.TopAdsShopItem
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DataFavoriteMapperTest {

    private val dataFavoriteMapper = DataFavoriteMapper()

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
        val viewModel = dataFavoriteMapper.prepareDataFavoriteShop(item)
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
        val viewModel = dataFavoriteMapper.prepareDataTopAdsShop(topAdsShop)
        assertNotNull(viewModel.adsShopItems)
        assertTrue(viewModel.adsShopItems!!.isEmpty())
    }

    @Test
    fun `prepareDataTopAdsShop with topAdsShop should return topAdsShowViewModel with correct topAdsShopItem list`() {
        val numOfItems = 2
        val topAdsShop = TopAdsShop(topAdsShopItemList = dummyTopAdsShopItemList(numOfItems))
        val viewModel = dataFavoriteMapper.prepareDataTopAdsShop(topAdsShop)

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
        val visitables = dataFavoriteMapper.prepareListFavoriteShop(favoriteShop)
        assertTrue(visitables.isEmpty())
    }

    @Test
    fun `prepareListFavoriteShop should return list of visitables with isFav is true`() {
        val numOfItems = 10
        val favoriteShop = FavoriteShop(data = dummyFavoriteShopItemList(numOfItems))
        val favoriteShopList = dataFavoriteMapper.prepareListFavoriteShop(favoriteShop)

        assertTrue(favoriteShopList.size == numOfItems)

        for (i in 0 until numOfItems) {
            assertTrue((favoriteShopList[i] as FavoriteShopViewModel).isFavoriteShop)
        }
    }

    private fun dummyFavoriteShopItemList(size: Int): List<FavoriteShopItem> {
        val items = ArrayList<FavoriteShopItem>()
        for (i in 0 until size) {
            items.add(FavoriteShopItem(name = randomString(20), isFav = false))
        }
        return items
    }

    private fun randomString(length: Int): String {
        val alphanum = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(length) { alphanum.random() }.joinToString("")
    }

    private fun dummyTopAdsShopItemList(size: Int): ArrayList<TopAdsShopItem> {
        val topAdsShopItemList = ArrayList<TopAdsShopItem>()
        for (i in 0 until size) {
            topAdsShopItemList.add(dummyTopAdsShopItem())
        }
        return topAdsShopItemList
    }

    private fun dummyTopAdsShopItem(): TopAdsShopItem {
        return TopAdsShopItem(
                shopId = randomString(10),
                shopDomain = randomString(10),
                shopName = randomString(10),
                adRefKey = randomString(10),
                shopClickUrl = randomString(10),
                shopImageCover = randomString(10),
                shopImageCoverEcs = randomString(10),
                shopImageUrl = randomString(10),
                shopImageEcs = randomString(10),
                shopLocation = randomString(10),
                isSelected = true
        )
    }

}
