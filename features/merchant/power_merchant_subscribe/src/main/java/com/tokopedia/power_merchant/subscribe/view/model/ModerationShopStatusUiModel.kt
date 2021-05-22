package com.tokopedia.power_merchant.subscribe.view.model

/**
 * Created By @ilhamsuaib on 22/05/21
 */

data class ModerationShopStatusUiModel(
        val shopStatusId: Int
) {

    companion object {
        private const val STATUS_MODERATED = 3
    }

    val isModeratedShop: Boolean
        get() = shopStatusId == STATUS_MODERATED
}