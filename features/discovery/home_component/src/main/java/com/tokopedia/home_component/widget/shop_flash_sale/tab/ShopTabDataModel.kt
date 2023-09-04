package com.tokopedia.home_component.widget.shop_flash_sale.tab

data class ShopTabDataModel(
    val id: String = "",
    val shopName: String = "",
    val imageUrl: String = "",
    val badgesUrl: String = "",
    val isActivated: Boolean = false,
) {
    companion object {
        const val PAYLOAD_ACTIVATED = "isActivatedChange"
    }
}
