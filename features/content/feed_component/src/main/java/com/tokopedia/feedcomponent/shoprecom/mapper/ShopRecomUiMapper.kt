package com.tokopedia.feedcomponent.shoprecom.mapper

import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.shoprecom.model.UserShopRecomModel

/**
 * created by fachrizalmrsln on 14/10/22
 **/
interface ShopRecomUiMapper {
    fun mapShopRecom(response: UserShopRecomModel, limit: Int): ShopRecomUiModel
    fun mapShopFollow(response: ShopFollowModel): MutationUiModel
}
