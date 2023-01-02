package com.tokopedia.feedcomponent.data.bottomsheet

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct

data class ProductBottomSheetData(
    val products: List<FeedXProduct>,
    val postId: String,
    val shopId: String,
    val postType: String,
    val isFollowed: Boolean,
    val hasVoucher: Boolean,
    val positionInFeed: Int,
    val playChannelId: String,
    val shopName:String,
    val mediaType: String,
    val saleType: String,
    val saleStatus: String,
    val authorType: String
) {
    val isFlashSaleToko: Boolean
        get() = saleType == ASGC_FLASH_SALE_TOKO
    val isRilisanSpl: Boolean
        get() = saleType == ASGC_RILISAN_SPECIAL
    val isUpcoming: Boolean
        get() = saleStatus == Upcoming
    val isOngoing: Boolean
        get() = saleStatus == Ongoing

    companion object {
        private const val ASGC_FLASH_SALE_TOKO = "asgc_flash_sale_toko"
        private const val ASGC_RILISAN_SPECIAL = "asgc_rilisan_spesial"
        private const val Upcoming = "upcoming"
        private const val Ongoing = "ongoing"

    }
}
