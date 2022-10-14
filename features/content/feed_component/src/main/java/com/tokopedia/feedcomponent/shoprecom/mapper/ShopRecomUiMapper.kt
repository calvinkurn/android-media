package com.tokopedia.feedcomponent.shoprecom.mapper

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.shoprecom.model.UserShopRecomModel

/**
 * created by fachrizalmrsln on 14/10/22
 **/
interface ShopRecomUiMapper {
    fun mapShopRecom(response: UserShopRecomModel): ShopRecomUiModel
}
