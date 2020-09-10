package com.tokopedia.home_component.model

/**
 * Created by Lukas on 10/09/20.
 */

data class ChannelShop(
        val id: String = "",
        val shopName: String = "",
        val shopBadgeUrl: String = "",
        val shopProfileUrl: String = "",
        val shopLocation: String = "",
        val isOfficialStore: Boolean = false,
        val isGoldMerchant: Boolean = false
)