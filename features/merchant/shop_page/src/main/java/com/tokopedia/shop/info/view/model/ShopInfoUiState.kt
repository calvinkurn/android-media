package com.tokopedia.shop.info.view.model

import com.tokopedia.shop.info.domain.entity.ShopEpharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformanceMetric
import com.tokopedia.shop.info.domain.entity.ShopRating
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment

data class ShopInfoUiState(
    val isLoading: Boolean = true,
    val shopImageUrl: String = "",
    val shopBadgeUrl: String = "",
    val shopName: String = "",
    val shopDescription: String = "",
    val mainLocation: String = "",
    val otherLocation: String = "",
    val operationalHours: String = "",
    val shopJoinDate: String = "",
    val rating: ShopRating = ShopRating(
        detail = emptyList(),
        positivePercentageFmt = "",
        ratingScore = "",
        totalRating = 0,
        totalRatingFmt = ""
    ),
    val review: ShopReview = ShopReview(
        totalReviews = 0,
        reviews = emptyList()
    ),
    val shopPerformanceMetrics: List<ShopPerformanceMetric> = emptyList(),
    val shopNotes: List<ShopNote> = emptyList(),
    val shipments: List<ShopSupportedShipment> = emptyList(),
    val showEpharmacyInfo: Boolean = false,
    val epharmacy: ShopEpharmacyInfo = ShopEpharmacyInfo(
        nearPickupAddressAppLink = "",
        nearestPickupAddress = "",
        pharmacistName = "",
        pharmacistOperationalHour = "",
        siaNumber = ""
    ),
    val error: Throwable? = null
)
