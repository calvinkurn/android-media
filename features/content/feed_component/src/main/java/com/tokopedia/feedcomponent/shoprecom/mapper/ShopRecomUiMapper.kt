package com.tokopedia.feedcomponent.shoprecom.mapper

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.shoprecom.model.UserShopRecomModel

interface ShopRecomUiMapper {
    fun mapShopRecom(response: UserShopRecomModel): ShopRecomUiModel
}
