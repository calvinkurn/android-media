package com.tokopedia.home_component.model

import com.tokopedia.analytics.byteio.PageName

data class ChannelTracker(
    // ByteIO
    val entranceForm: String = "",
    val sourceModuleType: String = "",
    val recomPageName: String = "",
    val layoutTrackerType: String = "",
    val productId: String = "0",
    val isTopAds: Boolean = false,
    val trackId: String = "",
    val recSessionId: String = "",
    val recParams: String = "",
    val requestId: String = "",
    val shopId: String = "0",
    val itemOrder: String = "",
    val layout: String = "",
    val cardName: String = "",
    val campaignCode: String = "",
    val creativeName: String = "",
    val creativeSlot: String = "",
    val isCarousel: Boolean = false,
    val categoryId: String = "",
    val productName: String = "",
    val recommendationType: String = "",
    val buType: String = "",

    // Additional
    val listName: String = "",
    val listNum: String = "0",

    // GTM
    val channelId: String = "",
    val channelName: String = "",
    val gridId: String = "",
    val headerName: String = "",
    val bannerId: String = "",
    val attribution: String = "",
    val persoType: String = "",
) {

    fun isProduct() = productId.isNotEmpty() && productId != "0"

    fun sourceModule() = "${sourceModuleType}_${PageName.HOME}_outer_${recomPageName}_module"

    fun getProperShopId() = if (isProduct()) shopId else "0"

    fun isAdsAsInt() = if (isTopAds) 1 else 0

    fun alwaysOnRemote() = 0
}
