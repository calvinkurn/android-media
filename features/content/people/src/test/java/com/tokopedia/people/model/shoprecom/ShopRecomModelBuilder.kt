package com.tokopedia.people.model.shoprecom

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem

class ShopRecomModelBuilder {

    val mockItemId: Long = 12345

    fun buildModelIsShown(): ShopRecomUiModel {
        return ShopRecomUiModel(
            isShown = true,
            nextCursor = "",
            title = "Toko lain sesuai rekomendasimu",
            items = generateShopItem()
        )
    }

    fun buildModelIsNotShown(): ShopRecomUiModel {
        return ShopRecomUiModel(
            isShown = false,
            nextCursor = "",
            title = "Toko lain sesuai rekomendasimu",
            items = generateShopItem()
        )
    }

    fun buildEmptyModel() = ShopRecomUiModel()

    fun buildEmptyItemModel() = ShopRecomUiModelItem()

    fun buildItem(isFollow: Boolean) = ShopRecomUiModelItem(
        badgeImageURL = "https://images.tokopedia.net/img/official_store_badge.png",
        encryptedID = "d7S/L5fmJQMHljR+nbg0iNvBGQ9U/DdZmuzd0bVvtakdXBg=",
        id = mockItemId,
        logoImageURL = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2020/11/16/ca4843f9-7b5c-468a-8be1-085d1f2b20c3.png",
        name = "Miisoo Official Shop",
        nickname = "miisooid",
        type = 2,
        applink = "tokopedia://shop/1353688",
        isFollow = isFollow,
    )

    private fun generateShopItem(): List<ShopRecomUiModelItem> {
        val shopRecomItem = mutableListOf<ShopRecomUiModelItem>()
        for (i in 0..9) {
            shopRecomItem.add(
                ShopRecomUiModelItem(
                    badgeImageURL = "https://images.tokopedia.net/img/official_store_badge.png",
                    encryptedID = "d7S/L5fmJQMHljR+nbg0iNvBGQ9U/DdZmuzd0bVvtakdXBg=",
                    id = if (i == 0) mockItemId else (i * 1000).toLong(),
                    logoImageURL = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2020/11/16/ca4843f9-7b5c-468a-8be1-085d1f2b20c3.png",
                    name = "Miisoo Official Shop",
                    nickname = "miisooid",
                    type = 2,
                    applink = "tokopedia://shop/1353688",
                    isFollow = false
                )
            )
        }
        return shopRecomItem
    }

}