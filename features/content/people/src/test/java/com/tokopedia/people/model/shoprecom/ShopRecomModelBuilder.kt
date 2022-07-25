package com.tokopedia.people.model.shoprecom

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem

class ShopRecomModelBuilder {

    fun buildModel(
        size: Int = 10,
    ): ShopRecomUiModel {
        return ShopRecomUiModel(
            isShown = true,
            nextCursor = "",
            title = "Toko lain sesuai rekomendasimu",
            items = MutableList(size) {
                ShopRecomUiModelItem(
                    badgeImageURL = "https://images.tokopedia.net/img/official_store_badge.png",
                    encryptedID = "d7S/L5fmJQMHljR+nbg0iNvBGQ9U/DdZmuzd0bVvtakdXBg=",
                    id = 1353688,
                    logoImageURL = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2020/11/16/ca4843f9-7b5c-468a-8be1-085d1f2b20c3.png",
                    name = "Miisoo Official Shop",
                    nickname = "miisooid",
                    type = 2,
                    applink = "tokopedia://shop/1353688",
                    isFollow = false
                )
            })
    }

    fun buildEmptyModel() = ShopRecomUiModel()

}