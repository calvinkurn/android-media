package com.tokopedia.shop.info.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.info.domain.entity.ShopInfo
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformance
import com.tokopedia.shop.info.domain.entity.ShopPerformanceDuration
import com.tokopedia.shop.info.domain.entity.ShopPharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopRating
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment

data class ShopInfoUiState(
    val isLoading: Boolean = true,
    val isLoadingShopReport: Boolean = false,
    val info: ShopInfo = ShopInfo(
        shopImageUrl = "",
        shopBadgeUrl = "",
        shopName = "",
        shopDescription = "",
        mainLocation = "",
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
        chatPerformance = ShopPerformanceDuration.Minute(Int.ZERO),
        orderProcessTime = ""
    ),
    val shopNotes: List<ShopNote> = emptyList(),
    val shipments: List<ShopSupportedShipment> = emptyList(),

    val pharmacy: ShopPharmacyInfo = ShopPharmacyInfo(
        showPharmacyInfoSection = false,
        nearestPickupAddressGmapsUrl = "",
        nearestPickupAddress = "",
        pharmacistName = "",
        pharmacistOperationalHour = emptyList(),
        siaNumber = "",
        sipaNumber = "",
        expandPharmacyInfo = true
    ),
    val error: Throwable? = null,
    val shopId: String = "",
    val districtId: String = "",
    val cityId: String = ""
)
