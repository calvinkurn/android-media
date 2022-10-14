package com.tokopedia.feedcomponent.shoprecom.mapper

import android.content.Context
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.shoprecom.model.UserShopRecomModel

class ShopRecomUiMapperImpl: ShopRecomUiMapper {
    override fun mapShopRecom(response: UserShopRecomModel): ShopRecomUiModel {
        return with(response.feedXRecomWidget) {
            ShopRecomUiModel(
                isShown = isShown,
                items = items.map {
                    ShopRecomUiModelItem(
                        badgeImageURL = it.badgeImageURL,
                        encryptedID = it.encryptedID,
                        id = it.id,
                        logoImageURL = it.logoImageURL,
                        name = it.name,
                        nickname = it.nickname,
                        type = it.type,
                        applink = it.applink,
                    )
                },
                nextCursor = nextCursor,
                title = title,
            )
        }
    }
}
