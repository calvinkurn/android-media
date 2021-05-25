package com.tokopedia.power_merchant.subscribe.view.model

/**
 * Created By @ilhamsuaib on 22/05/21
 */

data class ModerationShopStatusUiModel(
        val shopStatusId: Int
) {

    companion object {
        private const val STATUS_MODERATED = 3
        private const val STATUS_MODERATED_PERMANENTLY = 5
    }

    val isModeratedShop: Boolean
        get() = shopStatusId == STATUS_MODERATED || shopStatusId == STATUS_MODERATED_PERMANENTLY
}