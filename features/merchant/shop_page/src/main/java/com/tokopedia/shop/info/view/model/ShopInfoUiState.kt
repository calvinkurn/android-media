package com.tokopedia.shop.info.view.model

import com.tokopedia.shop.info.domain.entity.EpharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformanceMetric
import com.tokopedia.shop.info.domain.entity.ShopRatingAndReviews
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment

data class ShopInfoUiState(
    val shopImageUrl: String,
    val shopBadgeUrl: String,
    val shopName: String,
    val shopDescription: String,
    val mainLocation: String,
    val otherLocation: String,
    val operationalHours: String,
    val shopJoinDate: String,
    val ratingAndReview: ShopRatingAndReviews,
    val shopPerformanceMetrics: List<ShopPerformanceMetric>,
    val shopNotes: List<ShopNote>,
    val shipments: List<ShopSupportedShipment>,
    val showEpharmacyInfo: Boolean,
    val epharmacy: EpharmacyInfo
)
