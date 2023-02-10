package com.tokopedia.people.model.shoprecom

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem

class ShopRecomModelBuilder {

    val mockItemId: Long = 12345
    val mockEncryptedId: String = "d7S/L5fmJQMHljR+nbg0iNvBGQ9U/DdZmuzd0bVvtakdXBg="
    val typeShop = 2
    val typeBuyer = 3

    fun buildModelIsShown(type: Int = typeShop, isShown: Boolean = true, nextCursor: String = "123"): ShopRecomUiModel {
        return ShopRecomUiModel(
            isShown = isShown,
            nextCursor = if (isShown) nextCursor else "",
            title = if (isShown) "Toko lain sesuai rekomendasimu" else "",
            items = if (isShown) generateShopItem(type) else emptyList(),
            loadNextPage = if (isShown) nextCursor.isNotEmpty() && generateShopItem(type).isNotEmpty() else false,
            isRefresh = if (isShown) nextCursor.isEmpty() else false,
        )
    }

    fun buildEmptyModel() = ShopRecomUiModel()

    fun buildEmptyItemModel() = ShopRecomUiModelItem()

    private fun generateShopItem(type: Int = typeShop): List<ShopRecomUiModelItem> {
        val shopRecomItem = mutableListOf<ShopRecomUiModelItem>()
        for (i in 0..9) {
            shopRecomItem.add(
                ShopRecomUiModelItem(
                    badgeImageURL = "https://images.tokopedia.net/img/official_store_badge.png",
                    encryptedID = if (i == 0) mockEncryptedId else (i * 1000).toString(),
                    id = if (i == 0) mockItemId else (i * 1000).toLong(),
                    logoImageURL = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2020/11/16/ca4843f9-7b5c-468a-8be1-085d1f2b20c3.png",
                    name = "Miisoo Official Shop",
                    nickname = "miisooid",
                    type = type,
                    applink = "tokopedia://shop/1353688",
                    state = ShopRecomFollowState.UNFOLLOW,
                ),
            )
        }
        return shopRecomItem
    }
}
