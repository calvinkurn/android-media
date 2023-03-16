package com.tokopedia.feedcomponent.shoprecom.mapper

import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.shoprecom.model.UserShopRecomModel

/**
 * created by fachrizalmrsln on 14/10/22
 **/
class ShopRecomUiMapperImpl: ShopRecomUiMapper {

    override fun mapShopRecom(response: UserShopRecomModel, limit: Int): ShopRecomUiModel {
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
                loadNextPage = nextCursor.isNotEmpty() && items.isNotEmpty(),
                isRefresh = false,
            )
        }
    }


    override fun mapShopFollow(response: ShopFollowModel): MutationUiModel {
        return with(response.followShop) {
            if (success) MutationUiModel.Success()
            else MutationUiModel.Error(message)
        }
    }

}
