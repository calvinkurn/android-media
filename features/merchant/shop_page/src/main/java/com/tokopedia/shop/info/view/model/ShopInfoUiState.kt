package com.tokopedia.shop.info.view.model

import com.tokopedia.shop.info.domain.entity.ShopEpharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopInfo
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformance
import com.tokopedia.shop.info.domain.entity.ShopRating
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment

data class ShopInfoUiState(
    val isLoading: Boolean = true,
    val info : ShopInfo = ShopInfo(
        shopImageUrl = "",
        shopBadgeUrl = "",
        shopName = "",
        shopDescription = "",
        mainLocation = "",
        otherLocations = listOf(),
        operationalHours = emptyMap(),
        shopJoinDate = "",
        totalProduct = 0,
        shopUsp = listOf(),
        showPharmacyLicenseBadge = false
),
    val rating: ShopRating = ShopRating(
        detail = emptyList(),
        positivePercentageFmt = "",
        ratingScore = "",
        totalRating = 0,
        totalRatingFmt = "",
        totalRatingTextAndImage = 0,
        totalRatingTextAndImageFmt = ""
    ),
    val review: ShopReview = ShopReview(
        totalReviews = 0,
        reviews = emptyList()
    ),
    val shopPerformance: ShopPerformance = ShopPerformance(
        totalProductSoldCount = "",
        chatPerformance = "",
        orderProcessTime = ""
    ),
    val shopNotes: List<ShopNote> = emptyList(),
    val shipments: List<ShopSupportedShipment> = emptyList(),
    val showEpharmacyInfo: Boolean = false,
    val epharmacy: ShopEpharmacyInfo = ShopEpharmacyInfo(
        nearPickupAddressAppLink = "",
        nearestPickupAddress = "",
        pharmacistName = "",
        pharmacistOperationalHour = "",
        siaNumber = "",
        sipaNumber = "",
        collapseEpcharmacyInfo = true
    ),
    val error: Throwable? = null
)
