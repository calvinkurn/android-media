package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.shop.common.constant.ShopStatusDef

/**
 * Created By @ilhamsuaib on 22/05/21
 */

data class ModerationShopStatusUiModel(
        val shopStatusId: Int
) {

    val isModeratedShop: Boolean
        get() = shopStatusId == ShopStatusDef.MODERATED || shopStatusId == ShopStatusDef.MODERATED_PERMANENTLY
}