package com.tokopedia.home_component.widget.shop_tab

import com.tokopedia.kotlin.model.ImpressHolder

data class ShopTabDataModel(
    val id: String = "",
    val shopName: String = "",
    val imageUrl: String = "",
    val badgesUrl: String = "",
    val isActivated: Boolean = false,
    val useGradientBackground: Boolean = true
) : ImpressHolder() {
    companion object {
        const val PAYLOAD_ACTIVATED = "isActivatedChange"
    }
}
